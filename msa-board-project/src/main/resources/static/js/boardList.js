let excBtn = "next";
let idArr = [];
let firstId = -1;
let lastId = 0;

$(document).ready(()=>{
    checkToken();
    setupAjax();
    getUserInfo().then((userInfo)=>{
        $('#welcome-message').text(userInfo.userName + '님 환영합니다!');
        $('#hiddenUserName').val(userInfo.userName);
        $('#hiddenUserId').val(userInfo.userId);
    }).catch((error)=>{
        console.error('board list user info error :: ',error);
    });
    getBoards();
});

let getBoards = () => {
    let currentPage = 1;
    const pageSize = 10;

    loadNextBoards(currentPage,pageSize);

    $('#nextPage').on('click',()=>{
        currentPage++;
        excBtn = "next";

        loadNextBoards(currentPage,pageSize);
    });
    $('#prevPage').on('click',()=>{
        if(currentPage > 1){
            currentPage--;
            excBtn = "prev";

            loadPrevBoards(currentPage,pageSize);
        }
    })
}

let loadPrevBoards = (page,size) => {
    $.ajax({
        type : 'GET',
        url : '/api/board',
        data : {
            page : page,
            size : size,
            firstId : idArr[page],
            lastId : lastId,
            excBtn : excBtn
        },
        success : (response) => {
            console.log(response);
            $('#boardContent').empty();
            if(response.articles.length <=0 ){
                $('#boardContent').append(
                    `<tr>
                        <td colspan="4" style="text-align: center;">글이 존재하지 않습니다.</td>
                    </tr>`
                );
            }else{
                let idx = response.articles.length - 1;
                idArr.pop();
                firstId = response.articles[0].id;
                lastId = response.articles[idx].id;

                response.articles.forEach((article) => {
                    let transDate = article.created.split('T')[0];
                    console.log(transDate);
                    $('#boardContent').append(
                        `
                            <tr>
                                <td>${article.id}</td>
                                <td><a href="/detail?id=${article.id}">${article.title}</a></td>
                                <td>${article.userId}</td>
                                <td>${transDate}</td>
                            </tr>
                    `
                    );
                });
            }

            $('#pageInfo').text(page);
            $('#prevPage').prop('disabled',page === 1);
            $('#nextPage').prop('disabled', response.last);
        },
        error : (error) => {
            console.error('board list error :: ',error);
        }
    })
}

let loadNextBoards = (page,size) => {
    $.ajax({
        type : 'GET',
        url : '/api/board',
        data : {
            page : page,
            size : size,
            firstId : firstId,
            lastId : lastId,
            excBtn : excBtn
        },
        success : (response) => {
            console.log(response);
            $('#boardContent').empty();
            if(response.articles.length <=0 ){
                $('#boardContent').append(
                    `<tr>
                        <td colspan="4" style="text-align: center;">글이 존재하지 않습니다.</td>
                    </tr>`
                );
            }else{
                let idx = response.articles.length - 1;
                idArr.push(firstId + 1);
                firstId = response.articles[0].id;
                lastId = response.articles[idx].id;

                response.articles.forEach((article) => {
                    let transDate = article.created.split('T')[0];
                    console.log(transDate);
                    $('#boardContent').append(
                        `
                            <tr>
                                <td>${article.id}</td>
                                <td><a href="/detail?id=${article.id}">${article.title}</a></td>
                                <td>${article.userId}</td>
                                <td>${transDate}</td>
                            </tr>
                    `
                    );
                });
            }

            $('#pageInfo').text(page);
            $('#prevPage').prop('disabled',page === 1);
            $('#nextPage').prop('disabled', response.last);
        },
        error : (error) => {
            console.error('board list error :: ',error);
        }
    })
}

let logout = () => {
    $.ajax({
       type : 'POST',
       url : '/logout',
       success : () => {
           alert('로그아웃이 성공했습니다.');
           localStorage.removeItem('accessToken');
           window.location.href = '/member/login';
       },
        error : (error) => {
           console.error('logout error :: ',error);
           alert('로그아웃 중 오류가 발생했습니다.');
        }
    });
}
/*
let writeArticles = () => {
    $.ajax({
        type : 'POST',
        url : '/bomb',
        success : () => {
            window.location.href = '/';
        },
        error : (error) => {
            console.error('logout error :: ',error);
            alert('오류가 발생했습니다.');
        }
    });
}
 */
