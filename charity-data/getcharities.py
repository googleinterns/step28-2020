from urllib import parse
import requests
import json


def get_charity_link(base_charity_url):
    # Append / if not already present
    if base_charity_url[len(base_charity_url) - 1] != '/':
        base_charity_url += '/'
    # If /donate is valid
    if requests.get(base_charity_url + 'donate').status_code < 400:
        return base_charity_url + 'donate'
    # Otherwise, if homepage is valid
    elif requests.get(base_charity_url).status_code < 400:
        return base_charity_url
    else:
        return None


base_url = 'https://api.data.charitynavigator.org/v2/Organizations?'

# Parameters for request URL
app_id = '80de6d65'
app_key = '269363d5f87d1f1834f7b5ef5f9e4c3a'
pageSize = '1000'
rated = 'true'
sizeRange = '3'
sort = 'RATING'
params = {'app_id': app_id, 'app_key': app_key, 'pageSize': pageSize,
          'rated': rated, 'sizeRange': sizeRange, 'sort': sort}

url = base_url + parse.urlencode(params)

# Get JSON response body
charity_nav_json = requests.get(url).json()

# Counters for the number of charities with no valid donate link, the number
# with no valid homepage link, and the number with no valid image link.
num_no_valid_link = 0
num_no_image = 0

charities = []
for charity in charity_nav_json:
    # Create a charity JSON object
    processed_charity = {}
    
    # Add charity attributes to JSON object
    processed_charity['name'] = charity['charityName']

    try:
        if get_charity_link(charity['websiteURL']) != None:
            processed_charity['link'] = get_charity_link(charity['websiteURL'])
        else:
            num_no_valid_link += 1
    except requests.ConnectionError as e:
        print("Failed to open url " + charity['websiteURL'])
        num_no_valid_link += 1
        continue

    # NOTE: WE HAVE TO ATTRIBUTE CLEARBIT API:
    # <a href="https://clearbit.com">Logos provided by Clearbit</a>
    try:
        if requests.get('https://logo.clearbit.com/' + charity['websiteURL']).status_code < 400:
            processed_charity['image'] = 'https://logo.clearbit.com/' + charity['websiteURL']
        else:
            continue
    except requests.ConnectionError as e:
        print("Failed to open url " + 'https://logo.clearbit.com/' + charity['websiteURL'])
        num_no_image += 1
        continue

    processed_charity['category'] = charity['category']['categoryName']
    processed_charity['description'] = charity['mission']
    processed_charity['rating'] = charity['currentRating']['score']
    
    charities.append(processed_charity)

# Populate charities.json file with charities
with open("charities.json", "w") as outfile:
    json.dump(charities, outfile)