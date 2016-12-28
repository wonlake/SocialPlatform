//var root_url = 'http://localhost:8080';
var root_url = "";
vm = new Vue({
    el:"#app",
    data: {
        message:'hello vue!',
        todos : [
            {username:"java"},
            {username:"js"},
            {username:"html"}
        ],
        username: "",
        password: "",
        greeting: "",
        ok:false,
    }
});

function myclick() {
    var temp = vm.$http.get(root_url + '/user/friends?username=Lucy');
    temp.then(
        function(res) {
            var friends = JSON.parse(res.body.msg);
            vm.todos = [];
            for (var i=0; i<friends.length; i++) {
            }
        },
        function(res) {
            alert("无法获取数据!");
        }
    );
}