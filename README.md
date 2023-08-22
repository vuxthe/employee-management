# employee-management
Use Spring Boot 3 + Spring Security + MySql (docker)
Gồm các chức năng chính như:
- employee:
  + register, login, logout
  + checkin/checkout, read comment, project
- manager:
  + manage employee, project, comment
  + assign employee to project
- @Aynsc & emailService & Scheduler/crojob: remind checkin/checkout/notify to employee
- WebClient & restemplate to call 3rd api (weather api).
