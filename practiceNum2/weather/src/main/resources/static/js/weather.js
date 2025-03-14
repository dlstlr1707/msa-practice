let weather = () => {
    $.ajax({
        type: 'GET',
        url: '/weather',
        dataType : 'json',
        success: (response)=>{
            console.log(response);
            alert('요청이 성공했습니다.');
        },
        error: (error)=>{
            console.log('오류 발생 : ',error);
            alert('요청 중 오류가 발생했습니다.');
        }
    });
}