// importing dependencies
const  express = require('express')

// create router
const router = new express.Router()

// importing User model
const User = require('../models/user')

// create login route
router.post('/api/users/login', async (req, res) => {
    try {        
        // find user credentials
        const user = await User.findOne({username: req.body.username,password: req.body.password})

        // condition
        if (!user) {
            // send response
            return res.status(404).send(false);
        }

        // send response
        res.status(200).send(true)
    } catch (e) {
        // send response
        res.status(400).send(false)
    }
})

// export module
module.exports = router