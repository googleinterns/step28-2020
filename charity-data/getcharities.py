from urllib import parse
import requests
import json


base_url = 'https://api.data.charitynavigator.org/v2/Organizations?'

# Parameters for request URL
app_id = '80de6d65'
app_key = '269363d5f87d1f1834f7b5ef5f9e4c3a'
pageSize = '1000'
rated = 'TRUE'
sizeRange = '3'
sort = 'RATING'
params = {'app_id': app_id, 'app_key': app_key, 'pageSize': pageSize,
          'rated': rated, 'sizeRange': sizeRange, 'sort': sort}


def get_json(params):
    url = base_url + parse.urlencode(params)
    return requests.get(url).json()


def get_name(charity):
    return charity['charityName']


def get_link(base_charity_url):
    # Append / if not already present
    if base_charity_url[len(base_charity_url) - 1] != '/':
        base_charity_url += '/'
    # If /donate is valid
    if requests.get(base_charity_url + 'donate').status_code == 200:
        return base_charity_url + 'donate'
    # Otherwise, if homepage is valid
    elif requests.get(base_charity_url).status_code == 200:
        return base_charity_url
    else:
        return None


def get_image(charity):
    return 'https://logo.clearbit.com/' + charity['websiteURL']


def get_category(charity):
    return charity['category']['categoryName']


def get_cause(charity):
    if 'cause' in charity:
        return charity['cause']['causeName']
    else:
        return None
    

def get_description(charity):
    return charity['mission']


def get_rating(charity):
    if 'currentRating' in charity:
        return charity['currentRating']['score']
    else:
        return None
    


def process_charity(charity):
    # Create a charity JSON object
    processed_charity = {}

    # Add charity attributes to JSON object
    processed_charity['name'] = get_name(charity)
    
    global num_no_valid_link
    try:
        if get_link(charity['websiteURL']) is not None:
            processed_charity['link'] = get_link(charity['websiteURL'])
        else:
            num_no_valid_link += 1
            return None
    except requests.ConnectionError as e:
        print("Failed to open url " + charity['websiteURL'])
        num_no_valid_link += 1
        return None

    # NOTE: WE HAVE TO ATTRIBUTE CLEARBIT API:
    # <a href="https://clearbit.com">Logos provided by Clearbit</a>
    try:
        if requests.get(get_image(charity)).status_code == 200:
            processed_charity['image'] = get_image(charity)
        else:
            return None
    except requests.ConnectionError as e:
        print("Failed to open url " + get_image(charity))
        return None

    processed_charity['category'] = get_category(charity)
    processed_charity['cause'] = get_cause(charity)
    processed_charity['description'] = get_description(charity)
    
    if get_rating(charity) is not None:
        processed_charity['rating'] = get_rating(charity)
    else:
        return None

    return processed_charity


if __name__ == "__main__":
    # Counters for the number of charities without a valid donate or homepage link
    global num_no_valid_link
    num_no_valid_link = 0

    # Get JSON response body
    charity_nav_json = get_json(params)

    # Build a list of charity JSON objects
    charities = []
    for charity in charity_nav_json:
        processed_charity = process_charity(charity)
        if processed_charity is not None:
            charities.append(processed_charity)

    # Populate charities.json file with charities
    with open("charities.json", "w") as outfile:
        json.dump(charities, outfile)