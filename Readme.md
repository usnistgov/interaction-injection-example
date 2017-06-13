# Interaction Injector Example

This project shows how to inject interactions (interaction and objects) into a running HLA federation and how to implement an output class for receiving all interactions produced by the federation.

The implementation process consists of two steps:

1. Implement a class to serve as an entry point. (Has a main()). We'll call it the Driver.
2. Implement a reporter class to serve as a receiver for all published interactions. We'll call it the Reporter.

The Driver instantiates a Federate called InjectionFederate and starts it running in its own thread.  The InjectionFederate joins the federation, then waits for the federation to start.

The Driver then goes on to register and publish all interactions it intends to inject then 