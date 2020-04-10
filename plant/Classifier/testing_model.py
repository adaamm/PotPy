# I use this code to test the model

import matplotlib.pyplot as plt
import torch
from torchvision import transforms
from read_data import load_split_train_test, get_random_images
from predicting import predict_image
from data_dir import *

trainloader, testloader = load_split_train_test(data_dir, .2)
# print('Classes : ', trainloader.dataset.classes)


test_transforms = transforms.Compose([transforms.Resize([224, 224]),
                                      transforms.ToTensor(),
                                      ])
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = torch.load('plants.pth')
model.eval()

to_pil = transforms.ToPILImage()
images, labels = get_random_images(5, data_dir, test_transforms)
fig = plt.figure(figsize=(10, 10))
for ii in range(len(images)):
    image = to_pil(images[ii])
    index = predict_image(image, device, model, test_transforms)
    sub = fig.add_subplot(1, len(images), ii + 1)
    res = int(labels[ii]) == index
    sub.set_title(str(trainloader.dataset.classes[index]) + ":" + str(res))
    plt.axis('off')
    plt.imshow(image)
plt.show()
