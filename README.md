# SSL-CERT-CHECKER
### A service that notifies you by email of the expiration date of your SSL domain certificate.

---
# EMAIL SYSTEM :: INBOX
> [gmail.com] SSL Certificate ExpirationDate : 2024-05-27

> [mail.naver.com] SSL Certificate ExpirationDate : 2024-05-01
---
# Main Configuration 
## 1. Global Settings
```yml
[application.yml]
........
spring:
  main:
    web-application-type: none
  pid:
  file: checker.pid
checker:
  cron: 0 0 9 * * ?
  enablegroup:
    - custom-group-1
  server:
    host: 127.0.0.1
    port: 25
    ssl: false
    authentication: false
  mail:
    sender: noreply@yourdomain.com
    password: YOUR_ACCOUNT_PASSWORD
    text: SSL Certificate ExpirationDate
```
1. checker.**cron**: <code>String</code>
> Sets the frequency at which the server checks for SSL certificate expiration dates.
2. checker.**enablegroup**: <code>List`<String`></code>
> Sets the active groups among checker.groups.YOUR_GROUP_NAME.
3. checker.server.**host**: <code>String</code>
> Sets the hostname of the SMTP server to send notification emails.
4. checker.server.**port**: <code>int</code>
> Sets the port of the SMTP server to send notification emails. 
5. checker.server.**ssl**: <code>boolean</code>
> Sets whether to communicate with the SMTP server using STARTTLS.
6. checker.server.**authentication**: <code>boolean</code>
> Sets whether to authenticate with the SMTP server.
7. checker.mail.**sender**: <code>String</code>
> Sets the sender email address for notification emails.
8. checker.mail.**password**: <code>String</code>
> Sets the password of the sender email account for notification emails if authentication is enabled.
9. checker.mail.**text**: <code>String</code>
> Sets the text to be included in the subject and body of notification emails.
---
## 2. Group Settings
```yml
[application.yml]
........
........
checker:
........
........
  groups:
    custom-group-1:
      condition:
        - 7
        - 1
      recipients:
        - user1@domain.com
        - user2@domain.com
      domains:
        - mail.kakao.com
    custom-group-2:
      condition:
        - 30
        - 14
        - 7
        - 1
      recipients:
        - user1@domain2.com
      domains:
        - www.naver.com
        - www.google.com
```
1. checker.groups.**YOUR_GROUP_NAME**: <code>String</code>
> Sets the name of the group.
2. checker.groups.YOUR_GROUP_NAME.**condition**: <code>List`<Integer`></code>
> Sets the number of days before SSL certificate expiration to receive notifications for this group.
3. checker.groups.YOUR_GROUP_NAME.**recipients**: <code>List`<String`></code>
> Sets the email accounts to receive notification emails for this group.
4. checker.groups.YOUR_GROUP_NAME.**domains**: <code>List`<String`></code>
> Sets the domains to check SSL certificate expiration dates for. e.g., www.google.com
---
# How to Run?
```bash
java jar ssl-checker-1.0.0.jar
```