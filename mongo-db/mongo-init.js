db = db.getSiblingDB("expertsconseil");

db.createUser({
    user: "maher",
    pwd: "maher",
    roles: [
      {
        role: 'readWrite', 
        db: 'expertsconseil'
      },
    ],
  });

db.createCollection("users");

db.users.insertMany([
  {
    "firstName":"Test user firstName",
    "lastName":"Test user lastName",
    "email":"Test user email",
    "address":"Test user address",
    "birthday":"",
    "username":"Test user username",
    "password":"Test user password",
    "user_role":"ROLE_USER",
    "account_enabled":false,
    "account_expired":false,
    "account_locked":false,
    "credentials_expired":false
     }
]);