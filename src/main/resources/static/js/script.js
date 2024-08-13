$(function(){
    var $userRegister=$("#userRegister");

    $userRegister.validate({
        rules:{
            name:{
                required:true
            }
        },
        messages:{
            name:{
                required: 'Ad Soyad Boş Bırakılamaz'
            }
        }
    })
})
