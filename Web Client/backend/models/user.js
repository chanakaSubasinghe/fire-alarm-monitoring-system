// importing dependencies
const mongoose = require('mongoose');

// create new schema
const Schema = mongoose.Schema;

// create new user schema
const userSchema = new Schema({
    username: {
        type: String,
        lowercase: true,
        trim: true,
        required: true
    },
    password: {
        type: String,
        trim: true,
        lowercase: true,
        required: true
    }
})

// compiling schema to model
const User = mongoose.model('User',userSchema)

// export user
module.exports = User