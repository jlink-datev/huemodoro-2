## Installation

### Intelli J

1. Import Project

   File -> Project from Existing Sources...

2. Import Codestyle

    Preferences -> Editor -> Code Style -> Import Scheme -> Intelli J IDEA Codestyle XML: `huemodoro-codestyle.xml`


### Frontend

1. Update npm

    ```
    npm install -g npm
    ```

2. Install angular cli

    ```
    npm install -g @angular/cli
    ```

3. Install frontend app

    ```
    cd frontend/
    npm install
    ```

### Hue


Create file `application.properties` in your project root and use values from [setup hue](http://htmlpreview.github.io/?https://github.com/mklose/hue4junit/blob/master/setup_hue.html)  for it:

```
hue.host=<replace-with-hue-bridge-ip>
hue.client=<replace-with-username>
hue.lamp=1
```

## Build, Test and Run

### Test Backend

```
mvn verify
```

### Test Frontend

```
cd frontend/
npm test
```

### Build and Run Everything

```
./build-app.sh
./run-app.sh
```


# philips hue documentation

user: __datev_team_ws__  
password: __Geheim42__

https://developers.meethue.com/documentation/getting-started
https://developers.meethue.com/user

#find hue
https://www.meethue.com/api/nupnp
or 'curl https://www.meethue.com/api/nupnp'
http://<bridge ip address>/debug/clip.html

## trouble shooting
Should the json response contain an error, please make sure, that you have a valid user (see https://developers.meethue.com/documentation/getting-started under the section "So let’s get started…")

# rest calls

replace __$IP$__ and __$USERNAME$__ and __$LIGHT$__

## Green
```curl -H 'Accept: application/json' -X PUT -d '{"on":true, "sat":254, "bri":120, "hue":23000}'http://$IP$/api/$USERNAME$/lights/$LIGHT$/state```

## Red
```curl -H 'Accept: application/json' -X PUT -d '{"on":true, "sat":254, "bri":120, "hue":1}' http://$IP$/api/$USERNAME$/lights/$LIGHT$/state```


# more documentation

https://developers.meethue.com/develop/hue-api/lights-api/
https://www.burgestrand.se/hue-api/api/lights/#list-all-lights-and-their-names
