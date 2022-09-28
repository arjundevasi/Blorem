# Blorem
This app reminds user by their locations.

User selects a Location and and then sets a reminder.
When the user is or around specific location, the app send a notification.

##**The app uses Google Map SDK, Plcaes Api, and Geofence.**

How app works:
Maps are viewed in full screen using maps activity. An place autocomplete widget is used to guide the user in searching places.
On selecting the places or by dropping a pin on map, the user is then prompted to another activity.
Then user selects the radius by which he wants to get reminded.


##Screenshots from the app:
1.This is the main actvivty, Onclicking the Fab button the user is then taken to Mapview.
the reason this activity is empty is because here, I wanted to show the broadcasts that were registered.(Found out you cannnot know what broadcast you sent,apart from using SQL).
![192733573-6e831621-3a15-4198-bf6d-178e1c683aeb](https://user-images.githubusercontent.com/27912546/192734959-cd1fa812-c5d8-46e6-ab24-0cdc80a7ee8e.png)


2.Map View : The user then selects the place or location where he wants to put a reminder.
The app uses Maps places api to retrieve place name and suggest autocomplete places.
The app also used getlastknownlocation(), to automatically take user to his location on opeing app.
![192733645-a1585416-3e06-4db1-89cf-07ecbc2080be](https://user-images.githubusercontent.com/27912546/192735437-25cce83d-48a6-4295-bbfb-6e4fed8ea601.png)


3.On selecting the place, the app opens another activity.
In this activity, a Seekbar is present to adjust and set radius by which the user wants to set reminder.
The app also shows the place name and details if available.
![192733802-67227b85-7ea2-4b6f-a040-db552604d686](https://user-images.githubusercontent.com/27912546/192735908-2c1674a3-e341-44e8-9808-32d6be760119.png)

4.Once the user reaches the place or location, the broadcast reciever send a notification to user.
![192733873-1ac72f65-758f-4129-af4b-ccc53044f64d](https://user-images.githubusercontent.com/27912546/192736192-32bb0173-beb6-47bb-99ff-f8023b6d61b7.png)


Afterworks and features that can be added:
The app can also implement a exit transition, so when the user leaves a spot, the app can then do some task.
for example, a person who leaves his / her office can schedule the app to control his IoT devices. like turn on AC, cofeemachines,Temperature Control, etc.



