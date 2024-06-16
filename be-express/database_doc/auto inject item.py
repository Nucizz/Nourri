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
    Item('Beef', 1, 1.55882),
    Item('Cabbage', 1, 0.305),
    Item('Carrot', 1, 0.383333),
    Item('Chicken', 1, 1.40741),
    Item('Cucumber', 1, 0.14),
    Item('Egg', 1, 1.36364),
    Item('Eggplant', 1, 0.196667),
    Item('Leek', 1, 0.306667),
    Item('Onion', 1, 0.333333),
    Item('Pork', 1, 1.06195),
    Item('Potato', 1, 1.24),
    Item('Radish', 1, 0.2),
    Item('Tomato', 1, 0.2)
]

# API endpoint
url = "https://nourri-backend.vercel.app/add-ingredient"

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
