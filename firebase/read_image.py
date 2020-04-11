# Reading Image from firebase and converting it to the appropriate format
# The images on firebase are saved in a base 64 format
# We need to decode them in order to read them
import base64


# decode_image function takes a string argument
def decode_image(base64_image):
    image = base64.b64decode(base64_image)
    filename = 'tmpimage.jpg'  # tmp because the file will get overwritten every time this function is called
    with open(filename, 'wb') as file:
        file.write(image)

# Need another function that reads the string from firebase and passes it to the decode function
