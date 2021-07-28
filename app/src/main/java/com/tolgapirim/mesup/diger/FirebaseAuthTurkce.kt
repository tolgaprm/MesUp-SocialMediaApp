package com.tolgapirim.mesup.diger

class FirebaseAuthTurkce() {
private var mesaj=""
    fun TurkceMesaj(hataMesaji:String?): String {

        if(hataMesaji == "The provided email is already in use by an existing user. Each user must have a unique email."){
            mesaj = "Sağlanan e-posta zaten mevcut bir kullanıcı tarafından kullanılıyor."
        }else if (hataMesaji=="The provided value for the email user property is invalid. It must be a string email address."){
            mesaj ="Geçerli bir email adresi giriniz"
        }else if (hataMesaji=="The given password is invalid. [ Password should be at least 6 characters ]"){
            mesaj="Verilen şifre geçersiz. [ Şifre en az 6 karakter olmalıdır ]"
        }else if (hataMesaji=="The email address is badly formatted."){
            mesaj="Geçersiz e-posta adresi"
        }else if(mesaj=="There is no user record corresponding to this identifier. The user may have been deleted."){
            mesaj = "Bu Email'e sahip bir kullanıcı kaydı yok."
        }else{
            mesaj = hataMesaji!!
        }




        return mesaj
    }
}