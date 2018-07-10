# agony-engine
![AWS CodeBuild](https://codebuild.us-west-2.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoibHRwM2YxOVlSQmJWTzU4NE1BZC9UU1czK1hTaG5iTzRwUG1TS2QyT1RBTVNjTHhqUFNNenhwSkZHZFhraVJ4VkhkTWVhQVZHYWx6VEg1aXMzZEpUWkYwPSIsIml2UGFyYW1ldGVyU3BlYyI6Ii9RcC9Ob1ZMU0dvbi8yRnciLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master)
[![Website](https://img.shields.io/website-up-down-brightgreen-red/https/agonyengine.com.svg?label=website)](https://agonyengine.com)

**A modern, customizable engine for web based MUDs.**

## Quick Start
Please see the [Local Development](https://github.com/scionaltera/agony-engine/wiki/Local-Development) article in the Wiki to learn how to run The Agony Engine on your local machine and get your development environment started.

## Project Status
This is a brand new project, just getting started. The initial groundwork has been laid and development is beginning in earnest.

## Project Goals
### Flexible
Customization begins with a configuration file that offers many dials and levers to tweak the game. Each option is well explained. For many, that will be all the change that is necessary. To dig a little deeper, you can begin changing the website's templates, CSS and Javascript. For even more in-depth customization you have the code itself, which was designed with hacking and extensibility in mind. Rebuilding and testing locally is quick and easy. Deploying is as simple as pushing a Docker container.

### Modern
The Agony Engine uses the Spring Boot framework. It talks to the client over Websockets which allows for real time messages between the MUD and your browser, as well as out-of-band channels to support features such as status bars and gauges.

### Secure
Text based games have been around for several decades, but most of the MUDs out there today still use *telnet* as a protocol. On a technical level that is troubling because traffic (i.e. passwords) sent between players and the server is not encrypted, but on a more practical level it's just a very old protocol and there are simply better ways to do it now. The Agony Engine uses HTTPS for secure transport and BCrypt to ensure passwords are safe in the database.

### Traditional
The Agony Engine aims to be a "traditional" MUD codebase out of the box. It should feel pretty familiar to anyone who has played a DikuMUD derivative MUD before.

### Community Driven
A main goal of this project is to build a developer community around The Agony Engine. Contributions and feature requests are welcome, and hopefully it will become something that many others will find useful. That's why it is MIT licensed, truly free open source code. Grab a copy, try it out, and see what you can do with it.

## Get Involved
Please see the [contributing doc](https://github.com/scionaltera/agony-engine/blob/master/CONTRIBUTING.md) to learn how to make contributions to The Agony Engine. If you have questions, suggestions or bugs feel free to open a GitHub issue to discuss them!
