$(document).ready(()=>{

    $('#signin').click(()=>{
        let userId = $('#user_id').val();
        let password = $('#password').val();

        let formData = {
            username : userId,
            password : password
        }

        $.ajax({
            type : 'POST',
            url : '/login',
            data : $.param(formData),
            contentType : 'application/x-www-form-urlencoded; charset=utf-8',
            dataType : 'json',
            success : (response)=>{
                alert("로그인 성공!");
                window.location.href = "/member/weather";
            },
            error : (error) => {
                alert("로그인 실패!");
                console.log('오류 발생 : ',error);
                console.log(formData);
            }
        });
    });

});