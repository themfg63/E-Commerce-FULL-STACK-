$(function(){
    var $userRegister=$("#userRegister");

    $userRegister.validate({
        rules:{
            name:{
                required:true,
                lettersonly:true
            },
            email:{
                required: true,
                space: true,
                email: true
            },
            mobileNumber:{
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlenght: 11
            },
            password:{
                required: true,
                space: true
            },
            confirmpassword:{
                required: true,
                space: true,
                equalTo: '#pass'
            },
            address:{
                required: true,
                all: true
            },
            city:{
                required: true,
                space: true
            },
            state:{
                required: true,
            },
            pincode:{
                required: true,
                space: true,
                numericOnly: true
            },
            img:{
                required: true,
            }
        },
        messages:{
            name:{
                required: 'Ad Soyad Boş Bırakılamaz',
                lettersonly: 'Geçersiz Ad Soyad'
            },
            email:{
                required: 'Email Adresi Boş Bırakılamaz',
                space: 'Boşluk bırakılamaz',
                email: 'Geçersiz Email Adresi'
            },
            mobileNumber:{
                required: 'Telefon No Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz',
                numericOnly: 'Geçersiz Telefon Numarası',
                minlength: 'Telefon No En az 10 karakterden oluşmalı',
                maxlenght: 'Telefon No En fazla 11 karakterden oluşmalı'
            },
            password:{
                required: 'Şifre Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz'
            },
            confirmpassword: {
                required: 'Şifre Tekrarı Boş Bırakılamaz',
                space: 'Boşluk bırakılamaz',
                equalTo: 'Şifre Tekrarı Girilen Şifreyle Uyuşmuyor'
            },
            address:{
                required: 'Adres Bilgisi Boş Bırakılamaz',
                all: 'Geçersiz'
            },
            city: {
                required: 'Şehir Bilgisi Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz'
            },
            state:{
                required: 'Ülke Bilgisi Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz'
            },
            pincode: {
                required: 'Posta Kodu Bilgisi Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz',
                numericOnly: 'Geçersiz Posta Kodu'
            },
            img: {
                required: 'Profil Fotoğrafı Boş Bırakılamaz',
            }
        }
    })

    var $orders=$("#orders");

    $orders.validate({
        rules:{
            firstName: {
                required: true,
                lettersonly: true
            },
            lastName: {
                required: true,
                lettersonly: true
            },
            email: {
                required: true,
                space: true,
                email: true
            },
            mobileNo: {
                required: true,
                space: true,
                numericOnly: true,
                minlength: 10,
                maxlenght: 11
            },
            address: {
                required: true,
                all: true
            },
            city: {
                required: true,
                space: true
            },
            state: {
                required: true,
            },
            pincode: {
                required: true,
                space: true,
                numericOnly: true
            },
            paymentType: {
                required: true
            }
        },
        messages: {
            firstName: {
                required: 'Ad Bilgisi Boş Bırakılamaz',
                lettersonly: 'Geçersiz Ad Bilgisi'
            },
            lastName: {
                required: 'Soyad Bilgisi Boş Bırakılamaz',
                lettersonly: 'Geçersiz Soyad Bilgisi',
            },
            email: {
                required: 'Email Adresi Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz',
                email: 'Geçersiz Email Adresi'
            },
            mobileNo: {
                required: 'Telefon No Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz',
                numericOnly: 'Geçersiz Telefon No',
                minlength: 'Telefon No En az 10 karakterden oluşmalı',
                maxlenght: 'Telefon No En fazla 11 karakterden oluşmalı'
            },
            address: {
                required: 'Adres Bilgisi Boş Bırakılamaz',
                all: 'Geçersiz'
            },
            city: {
                required: 'Şehir Bilgisi Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz'
            },
            state: {
                required: 'Ülke Bilgisi Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz'
            },
            pincode: {
                required: 'Posta Kodu Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz',
                numericOnly: 'Geçersiz Posta Kodu'
            },
            paymentType: {
                required: 'Lütfen Ödeme Yöntemini Seçiniz'
            }
        }
    })

    var $resetPassword=$("#resetPassword");

    $resetPassword.validate({
        rules: {
            password: {
                required: true,
                space: true
            },
            confirmpassword: {
                required: true,
                space: true,
                equalTo: '#pass'
            }
        },
        messages: {
            password: {
                required: 'Şifre Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz'
            },
            confirmpassword: {
                required: 'Şifre Tekrarı Boş Bırakılamaz',
                space: 'Boşluk Bırakılamaz',
                equalTo: 'Şifre Tekrarı Girilen Şifreyle Uyuşmuyor'
            }
        }
    })
})

jQuery.validator.addMethod('lettersonly',function(value,element){
    return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
});

jQuery.validator.addMethod('space', function(value,element){
    return /^[^-\s]+$/.test(value);
});

jQuery.validator.addMethod('all', function(value,element){
    return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
});

jQuery.validator.addMethod('numericOnly', function(value,element){
    return /^[0-9]+$/.test(value);
});
