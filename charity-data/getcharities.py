from urllib import parse
import requests
import json

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
num_no_valid_donate = 0
num_no_valid_link = 0
num_no_image = 0

charities = []
for charity in charity_nav_json:
    # Create a charity JSON object
    processed_charity = {}
    
    # Name
    processed_charity['name'] = charity['charityName']

    # Link
    charity_url = charity['websiteURL']
    # Append / if not already present
    if charity_url[len(charity_url) - 1] != '/':
        charity_url += '/'
    try:
        # If /donate is valid
        if requests.get(charity_url + 'donate').status_code < 400:
            processed_charity['link'] = charity_url + 'donate'
        # Otherwise, if homepage is valid
        elif requests.get(charity_url).status_code < 400:
            num_no_valid_donate += 1
            processed_charity['link'] = charity_url
        else:
            continue
    except requests.ConnectionError as e:
        print("Failed to open url " + charity_url)
        num_no_valid_link += 1
        continue
    
    # Image
    # NOTE: WE HAVE TO ATTRIBUTE CLEARBIT API:
    # <a href="https://clearbit.com">Logos provided by Clearbit</a>
    try:
        if requests.get('https://logo.clearbit.com/' + charity_url).status_code < 400:
            processed_charity['image'] = 'https://logo.clearbit.com/' + charity_url
        else:
            continue
    except requests.ConnectionError as e:
        print("Failed to open url " + 'https://logo.clearbit.com/' + charity_url)
        num_no_image += 1
        continue

    # Category
    processed_charity['category'] = charity['category']['categoryName']
    # Description
    processed_charity['description'] = charity['mission']
    # Rating
    processed_charity['rating'] = charity['currentRating']['score']
    
    charities.append(processed_charity)

# Populate charities.json file with charities
with open("charities.json", "w") as outfile:
    json.dump(charities, outfile)