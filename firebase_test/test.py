 # * author : Philippe Vo 
 # * date : Feb-28-2020 09:16:42
 
# * Imports
# 3rd Party Imports
import pyrebase
# User Imports

# * Code
# firebase setup
config = {
  "apiKey": "AIzaSyD9Xl2rJi3YH3Ya7OwkyXzl3G9-FspA9Rk",
  "authDomain": "test-firebase-rasppi.firebaseapp.com",
  "databaseURL": "https://test-firebase-rasppi.firebaseio.com",
  "storageBucket": "test-firebase-rasppi.appspot.com"
  }

firebase = pyrebase.initialize_app(config)
auth = firebase.auth()
user = auth.sign_in_with_email_and_password("philippe.vo.nam@gmail.com", "secretPassword555") # you need to make a user in Firebase first (under Authentication)
db = firebase.database()

# before the 1 hour expiry:
user = auth.refresh(user['refreshToken']) # need this or we will have keyError

# Writing Data
data = {"value": 150}
db.child("sensor-data").child("moisture-data").set(data)

# Update Data
db.child("sensor-data").child("moisture-data").update({"value": 155})

# Read Data
all_users = db.child("sensor-data").get()
for user in all_users.each():
    print(user.key())
    print(user.val()['value'])