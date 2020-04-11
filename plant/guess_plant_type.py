from PIL import Image
from torch.autograd import Variable
from torchvision import transforms
#from plant.classifier.data_dir import *


def image_loader(image_name):
    """load image, returns cuda tensor"""
    imsize = 256
    loader = transforms.Compose([transforms.Scale(imsize), transforms.ToTensor()])
    image = Image.open(image_name)
    image = loader(image).float()
    image = Variable(image)
    image = image.unsqueeze(0)  # this is for VGG, may not be needed for ResNet
    return image.cpu()  # assumes that you're using GPU


def guess_type(image_path, model_path, trainloader):
    image = image_loader(image_path)
    model = model_path
    out = model(image)
    index = out.data.cpu().numpy().argmax()
    return trainloader.dataset.classes[index]
