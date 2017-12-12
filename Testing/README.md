# AWS Testing of the application

Our Application was tested for compatibility on the following devices-

![](https://github.com/kev5/Go-Meet/blob/master/Testing/AWS_Test.PNG)

3 tests were performed on each of the devices-

1. Setup Suite
2. Built-in Explorer Suite
3. Teardown Suite

The results were as follows-

![](https://github.com/kev5/Go-Meet/blob/master/Testing/DeviceTests.PNG)

The Built-in Explorer Suite in *Amazon Kindle Fire* and *LG G Pad* suffered a failure during the registration. We think it was because of failure to access the database properly, since all other devices successfully passed this test.
