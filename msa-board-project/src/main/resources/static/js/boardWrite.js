let selectedFile = null;

$(document).ready(()=>{
    checkToken();
    setupAjax();
    getUserInfo().then((userInfo)=>{
        $('#hiddenUserName').val(userInfo.userName);
        $('#hiddenUserId').val(userInfo.userId);
        $('#userId').val(userInfo.userId);
    }).catch((error)=>{
        console.error('board write user info error :: ', error);
    });

    $('#file').on('change',(event)=>{
        selectedFile = event.target.files[0];
        updateFileList();
    });

    $('#submitBtn').on('click',(event)=>{
        event.preventDefault();

        let formData = new FormData($('#writeForm')[0]);

        $.ajax({
           type : 'POST',
           url : '/api/board',
           data : formData,
           contentType : false,
           processData : false,
           success : () => {
                alert('게시글이 성공적으로 등록되었습니다.');
                window.location.href = '/';
           },
            error : (error) => {
               console.error('board write error :: ',error);
                if(error.status == 403){
                    window.location.href = "/access-denied";
                }else{
                    alert('게시글 등록 중 오류가 발새앴습니다.');
                }
            }
        });
    });
});

let updateFileList = () => {
    $('#fileList').empty();

    if(selectedFile){
        $('#fileList').append(
            `
                <li>
                    ${selectedFile.name} <button type="button" class="remove-btn">X</button>
                </li>
            `
        );

        $('.remove-btn').on('click',()=>{
           selectedFile = null;
           $('#file').val('');
           updateFileList();
        });
    }
}
