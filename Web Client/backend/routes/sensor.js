// importing dependencies
const express = require('express');

// create router
const router = new express.Router()

// importing Sensor model
const Sensor = require('../models/sensor')

// create route
router.post('/api/sensors', async (req, res) => {

    try {
        // create new sensor
        const sensor = new Sensor(req.body);

        // save it in db
        await sensor.save()

        // send response
        res.status(201).send(true)
    } catch (e) {
        // send response
        res.status(400).send(false)        
    }
})

// read all route
router.get('/api/sensors', async (req, res) => {
    try {

        // assigning all sensors in the database
        const sensors = await Sensor.find({});

        // send response
        res.status(200).send(sensors)
    } catch (error) {
        // send response
        res.status(400).send(error.message)        
    }
})

// read one sensor
router.get('/api/sensors/:id', async (req, res) => {

    // assigning provided id
    const _id = req.params.id

    try {
        
        // find specific sensor
        const sensor = await Sensor.findOne({_id})

        // condition
        if (!sensor) {
            // throw new error
            return res.status(404).send('not found')
        }

        // send response
        res.status(200).send(sensor)
    } catch (e) {
        // send response
        res.status(400).send(e.message)
    }
})

// edit route
router.put('/api/sensors/:id', async (req, res) => {

    // assigning provided id
    const _id = req.params.id

    try {

        // find specific sensor
        const sensor = await Sensor.findOneAndUpdate({_id},req.body)
  
        // condition
        if (!sensor) {
            // send response
            return res.status(404).send('not found')
        }

        // save back to the database
        await sensor.save()

        // send response
        res.status(200).send(true)
    } catch (e) {
        // send response
        res.status(400).send(false)
    }
})

// delete route
router.delete('/api/sensors/:id', async (req, res) => {
    // assigning provided id
    const _id =  req.params.id

    try {
        
        // delete sensor
        const sensor = await Sensor.findOneAndRemove({_id})

        // condition
        if (!sensor) {
            // return and send response
            return res.status(404).send('not found')
        }

        // send response
        res.status(200).send(true)

    } catch (e) {
        // send response
        res.status(400).send(false)
    }
})

// exporting router
module.exports = router