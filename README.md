### About :

Electricity Meter Status detection based on Optical Character Recognition and Image Processing
There is two external libraries used in this Project : "tess-two" for OCR and "OpenCV" for the Image Proecessing.

There is two methods implemented in this application the first one is :

     -Using a custom Camera with a drawn box on the canvas that covers the surface of the meter. 
  
     -Place the box over the meter and take a picture to crop automatically the wanted part and detect the numbers
  
     -Result is not accurate. The quality of the croped image needs to be optimised.

The second method is : 

     -Take a picture with the default camera.
  
     -Crop manually the picture to start the Image Processing and the OCR automatically in the background.
  
     -Result is accurate.
  
  
### Screenshots -- Test Object used is a kitchen scale which have the same display as an electricity meter :

## First Method :

<img src="https://user-images.githubusercontent.com/31047155/31039068-e707e11c-a57a-11e7-9fe1-c3881ddfe666.png" width="200">    <img 
src="https://user-images.githubusercontent.com/31047155/31039067-e7075d78-a57a-11e7-9572-7b204265974b.png" width="200">

## Second Method :

<img src="https://user-images.githubusercontent.com/31047155/31039266-b0f0a86e-a57c-11e7-9d71-ce99e0f3047c.png" width="200">    <img 
src="https://user-images.githubusercontent.com/31047155/31039268-b0f3eb96-a57c-11e7-851e-ea1590dae9ca.png" width="200"> <img 
src="https://user-images.githubusercontent.com/31047155/31039267-b0f344d4-a57c-11e7-9ec1-ca03dbb80355.png" width="200">
