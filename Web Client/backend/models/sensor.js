// importing dependencies
const mongoose = require('mongoose')

// create new schema
const Schema = mongoose.Schema;

// create new sensor schema
const sensorSchema = new Schema({
    name: {
        type: String,
        required: true,
        trim:true,
        lowercase: true        
    },
    floorNumber: {
        type: Number,
        required: true
    },
    roomNumber: {
        type: Number,
        required:true,
        unique: true
    },
    carbonDioxideLevel: {
        type: Number,
        default: 0
    },
    smokeLevel: {
        type: Number,
        default: 0
    }
})

// compiling schema to model
const Sensor = mongoose.model('Sensor', sensorSchema);

// exporting sensor
module.exports = Sensor;
