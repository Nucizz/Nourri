import requests
import json
import time

class Item:
    def __init__(self, name, weight, ccal):
        self.name = name
        self.weight = weight
        self.ccal = ccal

# Define the items and add them to the item_list
item_list = [

]

# API endpoint
url = "http://localhost:3000/add-ingredient"

# Headers for the POST request
headers = {"Content-Type": "application/json"}

# Loop to send POST requests with item_list
for item in item_list:
    # JSON body
    payload = {
        "name":  item.name,
        "ccal":  item.ccal / item.weight
    }

    # Convert payload to JSON string
    json_payload = json.dumps(payload)

    # Make the POST request
    response = requests.post(url, data=json_payload, headers=headers)

    # Print response status code and content (optional)
    print(f"Item: {item.name}, Status Code: {response.status_code}, Response: {response.text}")

    # Wait for a moment before sending the next request (e.g., 1 second)
    time.sleep(1)
