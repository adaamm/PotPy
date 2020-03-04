 # * author : Philippe Vo 
 # * date : Feb-28-2020 09:16:42
 
# * Imports
# 3rd Party Imports
import pyrebase
import time
# User Imports

# * Code
# firebase setup
config = {
  "apiKey": "AIzaSyAeLC1SPoeniyf98IpanvSTzFc_Yh_DS1w",
  "authDomain": "coen390-guarduino.firebaseapp.com",
  "databaseURL": "https://coen390-guarduino.firebaseio.com/",
  "storageBucket": "coen390-guarduino.appspot.com"
  }

firebase = pyrebase.initialize_app(config)
auth = firebase.auth()
user = auth.sign_in_with_email_and_password("philippe.vo.nam@gmail.com", "secretPassword555") # you need to make a user in Firebase first (under Authentication)
db = firebase.database()

# before the 1 hour expiry:
user = auth.refresh(user['refreshToken']) # need this or we will have keyError

# Writing Data
# data = {"value": 150}
# db.child("Plant1").child("moisture").set(data)

# Update Data
for cycle in range(100000):
  time.sleep(1)
  print("Updating : " + str(cycle))
  db.child("Plant1").update({"moisture": cycle})

# Read Data
# all_users = db.child("Plant1").get()
# for user in all_users.each():
#     print(user.key())
#     print(user.val()['value'])