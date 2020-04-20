# By Adam Diallo , April 7th 2020

# Imports
import matplotlib.pyplot as plt
import torch
from torch import nn
from torch import optim
from torchvision import models
# User import
from read_data import load_split_train_test
from data_dir import *

# Reading the data, splitting and loading it
trainloader, testloader = load_split_train_test(data_dir, .2)
print('Classes : ', trainloader.dataset.classes)

# Loading the pre-trained model
# we use Resnet50
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = models.resnet50(pretrained=True)
# print(model)

for param in model.parameters():
    param.requires_grad = False

model.fc = nn.Sequential(nn.Linear(2048,512),
                         nn.ReLU(),
                         nn.Dropout(0,2),
                         nn.Linear(512,len(trainloader.dataset.classes)),
                         nn.LogSoftmax(dim=1))

# Loss criterion
criterion = nn.NLLLoss()
optimizer = optim.Adam(model.fc.parameters(), lr = 0.003) # We use the Adam Model
model.to(device)

epochs = 1
steps = 0
running_loss = 0
print_every = 10
train_losses , test_losses = [],[]

for epoch in range(epochs):
    for inputs, labels in trainloader:
        steps += 1
        inputs, labels = inputs.to(device), labels.to(device)
        optimizer.zero_grad()
        logps = model.forward(inputs)
        loss = criterion(logps, labels)
        loss.backward()
        optimizer.step()
        running_loss += loss.item()

        if steps % print_every == 0:
            test_loss = 0
            accuracy = 0
            model.eval()
            with torch.no_grad():
                for inputs, labels in testloader:
                    inputs, labels = inputs.to(device), labels.to(device)
                    logps = model.forward(inputs)
                    batch_loss = criterion(logps, labels)
                    test_loss += batch_loss.item()
                    ps = torch.exp(logps)
                    top_p, top_class = ps.topk(1, dim=1)
                    equals = labels.view(*top_class.shape)
                    accuracy += torch.mean(equals.type(torch.FloatTensor)).item()
                    train_losses.append(test_loss / len(testloader))
                    test_losses.append(test_loss / len(testloader))
                    print(f"Epoch{epoch + 1}/{epochs}.."
                          f"Train loss : {running_loss / print_every:.3f}.."
                          f"Test loss: {test_loss / len(testloader):.3f}.. "
                          f"Test accuracy: {accuracy / len(testloader):.3f}")
                    running_loss = 0
                    model.train()
                    torch.save(model, 'plants.pth')

plt.plot(train_losses, label='Training loss')
plt.plot(test_losses, label='Validation loss')
plt.legend(frameon=False)
plt.show()