import mysql from "mysql"

// export const db = mysql.createConnection({
//     host: 'mysql://1uw5pnqnxm3zcov92u3j:pscale_pw_5hIzQ7e9xdbIlNEWB4k31G1BttB4GQ3FhtArnY0F2AT@aws.connect.psdb.cloud/dogs?ssl={"rejectUnauthorized":true}',
//     user: "1uw5pnqnxm3zcov92u3j",
//     password: "pscale_pw_5hIzQ7e9xdbIlNEWB4k31G1BttB4GQ3FhtArnY0F2AT",
//     database: "dogs"
// })

// require('dotenv').config()
// const mysql = require('mysql2')

// export const db = mysql.createConnection('mysql://inzxh6mubdq60aixv6oh:pscale_pw_1ZuklTdsvyCng81q0P4LInRS8agTpmzaUB5gLCtYxGw@aws.connect.psdb.cloud/dogs?ssl={"rejectUnauthorized":true}')



// Se der erro local é pq a valiavel .env não ta indetificando no process.env.DATABASE_URL
export const db = mysql.createConnection(process.env.DATABASE_URL)
console.log('Connected to!')




//connection.end()
  

/*
export const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "admin",
    database: "dogs"
})

// npm install mysql2

require('dotenv').config()

const mysql = require('mysql2')

// Create the connection to the database
const connection = mysql.createConnection(process.env.DATABASE_URL)

// simple query
connection.query('show tables', function (err, results, fields) {
  console.log(results) // results contains rows returned by server
  console.log(fields) // fields contains extra metadata about results, if available
})

// Example with placeholders
connection.query('select 1 from dual where ? = ?', [1, 1], function (err, results) {
  console.log(results)
})

connection.end()


*/