package com.rocketman19.psychologistconsultationapplication

class appTherapist {
    //creating an appTherapists table in the firebase database
    var inputName:String = ""
    var inputPhone: String = ""
    var inputEmail:String = ""
    var inputLocation:String = ""
    var inputQualification:String = ""
    var inputPassword:String = ""
    var id:String = ""
    var inputPhoto:String = ""

    constructor(inputName:String, inputEmail:String, inputPhone: String, inputLocation:String, inputQualification:String, inputPassword:String, id:String,inputPhoto:String) {
        this.inputName = inputName
        this.inputPhone = inputPhone
        this.inputEmail = inputEmail
        this.inputLocation = inputLocation
        this.inputQualification = inputQualification
        this.inputPassword = inputPassword
        this.id = id
        this.inputPhoto = inputPhoto
    }
    constructor(){}
}

