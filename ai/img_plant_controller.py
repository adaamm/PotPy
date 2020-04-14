#!/usr/bin/env python3

import base64

from PIL import Image
from torch.autograd import Variable
from torchvision import transforms
#from plant.classifier.data_dir import *
import torch

from ai.classifier.read_data import load_split_train_test

# decode_image function takes a string argument
def decode_image(base64_image):
    image = base64.b64decode(base64_image)
    filename = 'tmpimage.jpg'  # tmp because the file will get overwritten every time this function is called
    with open(filename, 'wb') as file:
        file.write(image)

    return filename
# Need another function that reads the string from firebase and passes it to the decode function

def image_loader(image_name):
    """load image, returns cuda tensor"""
    imsize = 224
    loader = transforms.Compose([transforms.Resize([224, 224]),
                                      transforms.ToTensor(),
                                      ])
    image = Image.open(image_name)
    image = loader(image).float()
    image = Variable(image)
    image = image.unsqueeze(0)  # this is for VGG, may not be needed for ResNet
    return image.cpu()  # assumes that you're using GPU

def guess_type(image_path, model_path, trainloader):
    to_pil = transforms.ToPILImage()
    image = image_loader(image_path)
    image = to_pil(image)
    model = model_path
    out = model(image)
    model.eval()
    index = out.data.cpu().numpy().argmax()
    return trainloader.dataset.classes[index]

def run_img_guessing(base64_image):
    """
    decodes the input image -> then guess the plant -> outputs the name of that plant

    Arguments
    ---------
    base64_image : str
        image in base64 format

    Returns
    -------
    plantName : str
        name of plant guessed
    """
    dataDir = "ai/data/plants"
    modelPath = "ai/classifier/plants.pth"

    trainloader, testloader = load_split_train_test(dataDir, 0.2)

    imgFilePath = decode_image(base64_image)
    model = torch.load(modelPath)
    model.eval()
   
    plantName = guess_type(imgFilePath, model, trainloader)

    return plantName
