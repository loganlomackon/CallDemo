# CallDemo
Objective:
Solve call passing problem. 
The main logic is implemented in IncomingCallService.acceptCall.

Feature:
1.Configure whatever job title you want and related call passing logic in JobLevel.java.
2.When an employee is receiving other's call, his/her "busy" status would be managed in Redis cache, 
for it's just short-time and temporary state.

Prerequisite:
Java OpenJDK1.8
Redis server installed at localhost
Mysql server installed at localhost

Testing:
1.Create new employee:
POST {ServerUrl}/api/employee
{
  "job_level": //Include "Fresher", "Team Lead", and "PM"
  "name": 
}

2.Show all employees in database and busy status.
POST {ServerUrl}/api/employee/list

3.Simulate an incoming call
POST {ServerUrl}/api/call
{
   "caller":
   "content"
}

Actually a phone call would be three-sides duplex communication:
A client side application for a caller, a server handling call passing, and a client side application for an employee.
Here we skip the client-server communication part(which might be implemented by websocke).

When an empoyee "pick up" call and read the content, I assume it takes 3 seconds to process the problem.
If there is no "Fresher" available or problem cannot be solved, the call will be passed to higher level employee, until the call is answered. If a PM accept a call, he or she must give an answer for there is no one to pass calls.
This can be configured in JobLevel.java too.
