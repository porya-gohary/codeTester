<h1 align="center">
  Code Tester
</h1>
<h4 align="center">A simple java program to give test cases to C++ and python codes.</h4>
<p align="center">
  <a href="#-dependencies">Dependencies</a> •  
  <a href="#-build-instructions">Build</a> •
<a href="#-contribution">Contribution</a> •
  <a href="#-license">License</a>
</p>
<p align="center">
  <a href="https://github.com/porya-gohary/codeTester/blob/master/LICENSE.md">
    <img src="https://img.shields.io/hexpm/l/apa"
         alt="Gitter">
  </a>
    <img src="https://img.shields.io/badge/Built%20with-JavaFX-orange">

</p>
<h1 align="center">
  <a href="https://postimg.cc/9D9pbzpn"><img src="https://i.postimg.cc/XYHzVCy4/Ubuntu-2021-11-08-19-10-56.png" alt="CodeTester" width="500"></a>
</h1>

## 📦 Dependencies

- [JDK 16 or later](http://jdk.java.net/) (Make sure `JAVA_HOME` is properly set to the JDK installation directory.)
- [Maven 3.8.3 or later](https://maven.apache.org/)

## 📋 Build Instructions
These instructions assume a Linux host.

 This project use Maven build system. For install Maven follow instruction [here](https://maven.apache.org/install.html). 
 This program tested with `Maven 3.8.3`.
 
First, clone the git repository:
```
$ git clone https://github.com/porya-gohary/codeTester.git
```
Redirect to `codeTester` folder:
```
$ cd codeTester
```
Now for running the application use the following command:
```
$ mvn clean javafx:run
```
Also, for making `jar` file use the following command:
```
$ mvn package
```
Next for running `jar` file redirect to `target` folder and execute the `jar` file:
```
$ cd target
$ java -jar codeTester-1.0-SNAPSHOT.jar
```
## 🌱 Contribution
With your feedback and conversation, you can assist me in developing this application.
- Open pull request with improvements
- Discuss feedbacks and bugs in issues

## 📜 License
Copyright © 2021 [Pourya Gohari](https://pourya-gohari.ir)

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details