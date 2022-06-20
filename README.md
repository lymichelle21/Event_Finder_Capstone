# Capstone Project - App Name Pending

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
App allows users to find events near them and upload and view photos. This app is basically a combination of event finding in EventBrite and photo sharing in Google Drive in a mobile-friendly way. This can be useful for events where people want to share photos like graduation, concerts, and hackathons.

### App Evaluation
- **Category:** Social Networking / Photo / Entertainment
- **Mobile:** Mobile-first experience
- **Story:** Allows users to find events near them and share photos of the event.
- **Market:** This app is for everyone, especially those looking for entertainment and to connect with others through events. The photo sharing feature of ths app is especially great for large community events. 
- **Habit:** This app could be used as much as the user wants depending on their interest in attending events
- **Scope:** Start with core features like seeing events, finding events, and being able to upload photos from the event. Then, expand on features such as sorting events, exploring events on a map, integrating calendars and SMS, among others. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can log in or register for an account to access their profile
    * User can view their profile with their username and bookmarked events
    * User can add types of events they're interested in to their profile and that gets factored in to algorithm of recommended events
* User can scroll through events near them
    * **User can view events, which are cached locally in a unified model**
    * User can filter events by category
    * User can double tap on events to bookmark them
    * User can click on an event to see details screen
* **User can get recommended events based on their past data**
    * User can see a feed of recommended events and click on event to see details screen
* User can upload and view photos from the event
    * User can add a caption when uploading photos
    * User can select a photo to view and see a growing animation
* User sees animated splash screen when opening app

**Bolding** denotes difficult/ambiguous technical problems

**Optional Nice-to-have Stories**

* User can view events near them on a map
    * **User can zoom out and see events near each other combine into one pin**
    * User can tap on events on the map to get pop up details and navigate to detail screen
    * User can pinch to zoom on map
* User can look up events by name
* User can refresh feed
* User can swipe left if they are not interested in the event
* User can swipe right if they are interested in the event (which bookmarks them)
* User can scroll infinitely
* User can edit their username and password
* User can see time stamp for photo
* User can get notified when an event they bookmarked is happening soon
* User can invite friends to events by clicking a button to launch SMS with prefilled info
* User can add an event to their calendar
* User can create a collage of photos in an album
* User can use the map to get directions to an event
* User can get recommended events based on their past bookmarked events
   * User can add types of events they're interested in to their profile and get recommended events in those categories
* User can get info about the organizer of the event
   * User can click a button to contact the organizer of the event
* User can filter events by selected time frame
* User can set a price range for events they want to see
* User can toggle between light mode and dark mode

### 2. Screen Archetypes

* Login/Registration Screen
   * User can log in or register for an account
* Stream
   * User can view a feed of events
   * User can explore events by category
   * User can view photos for an event
   * User can double tap events to bookmark them
* Detail
   * User can view event details
   * User can click on event on map to get details pop up
* Creation 
   * User can add a photo for an event
* Maps
   * User can view events on a map
   

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Events Stream
* Profile
* Recommendations
* Map

**Flow Navigation** (Screen to Screen)
* Events Stream
   * Event Details 
      * Photo Album
      * Add Photo
      * SMS
* Map
   * Event Details
* Profile - Bookmarked Events
   * Event Details  
* Recommendations
   * Event Details

## Wireframes
<img src="wireframe1.jpeg" width=600>

<img src="wireframe2.jpeg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

### Models

Photo

| Property     | Type      | Description     |
| ------------ | --------- | ----------------|
| objectId | String | unique id for the photo (default field) |
| user	| Pointer to User| user |
| event| String | event id from API that photo is from |
| image | File | image that user posts |
| caption | String | photo caption by user |
| createdAt | DateTime | date when photo is created (default field) |
| updatedAt | DateTime | date when photo is last updated (default field) |

User
| Property     | Type      | Description     |
| ------------ | --------- | ----------------|
| objectId | String | unique id for the user (default field) |
| username	| String | user's account username |
| password	| String | user's account password |
| zip	| Number | user zip code |
| bookmark	| Array | array of event ids that the user bookmarked |
| bookmark	| Array | array of event ids that the user bookmarked |
| createdAt | DateTime | date when account is created (default field) |
| updatedAt | DateTime | date when account is last updated (default field) |
| ACL | ACL | permissions (default field) |


### Networking
* Event Feed Screen
   * (Read/GET) Query all events
   * (Create/POST) Bookmark an event
   * (Delete) Delete existing bookmark
* Add Photo Screen
   * (Create/POST) Create a new photo object and caption
* Profile Screen
   * (Read/GET) Query logged in user object
   * (Update/PUT) Update user's username, profile image, password
   * (Read/GET) Query bookmarked posts
   * (Delete) Delete existing bookmark
* Map Screen
   * (Read/GET) Query events near user

### Parse Query Code Snippet Example - querying photos for each event
```java
    private void queryPhotos() {
        //TODO: only query photos from selected event
        ParseQuery<Photo> query = ParseQuery.getQuery(Photo.class);
        query.findInBackground(new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Failed to get photos", Toast.LENGTH_LONG).show();
                    return;
                }
                for (Photo photo : photo) {
                    return;
                }
            }
        });
    }
```
