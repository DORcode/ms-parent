<template>
  <div id="ago">
    <h1>ago</h1>
  </div>
</template>

<script>
export default {
  name: 'Ago',
  props: {
    msg: String
  },

  data: function () {
    // Authorization Bearer
    // this.test_seperate_sso();

    // const a = 'http://localhost:8090/oauth/authorize?client_id=ms-client&redirect_uri=http://localhost:8281/client/login&response_type=code';
    // window.location.href = a

    // this.accessToken();
    // this.getCode();
    const a = 'http://client2.com:8281/client/callback'
    window.location.href = a
  },

  methods: {
    test_seperate_sso: function () {
      console.log('开始');
      this.$axios.get('http://localhost:8281/client').then(
        function (response) {
          console.log(response);
        }
        ).catch(function (error) {
          console.log(error);
        })
    },

    getCode: function () {
      let code = this.urlSearch('code')
      // let state = this.urlSearch('state')

      // 如果不存在 token，
      if(!localStorage.getItem('access_token')) {
        console.log('获取code')
        // const a = 'http://localhost:8090/oauth/authorize?client_id=ms-client&redirect_uri=http://localhost:8281/client/skip&response_type=code';
        const a = 'http://localhost:8281/client/skip'
        window.location.href = a
        // window.open(a, '_blank');
      } else if(!code && localStorage.getItem('access_token')) {
        let type = localStorage.getItem('token_type')
        let token = localStorage.getItem('access_token')
        // 没有code参数，并且存在access_token时，
        this.$axios.get(`http://localhost:8281/client/user`, {headers:{ Authorization: `${type} ${token}` }}).then(
          function (response) {
            console.log(response);
            if(response.data) {
              let token = response.data.access_token
              let expires_in = response.data.expires_in
              let token_type = response.data.token_type
              localStorage.setItem('access_token', token);
              localStorage.setItem('expires_in', expires_in);
              localStorage.setItem('token_type', token_type);
            }
          }
          ).catch(function (error) {
            console.log(error);
          })
      } else if(code && !localStorage.getItem('access_token')) {
        console.log('获取access_token')
        // 当
        this.$axios.get(`http://localhost:8281/client/login?code=${code}&redirect_uri=http://localhost:3001`).then(
        function (response) {
          console.log(response);
          if(response.data) {
            let token = response.data.access_token
            let expires_in = response.data.expires_in
            let token_type = response.data.token_type
            localStorage.setItem('access_token', token);
            localStorage.setItem('expires_in', expires_in);
            localStorage.setItem('token_type', token_type);
          }
        }
        ).catch(function (error) {
          console.log(error);
        })
      }
    },

    accessToken: function () {
      let code = this.urlSearch('code')
      let state = this.urlSearch('state')
      
      this.$axios.get(`http://localhost:8281/client/accessToken?code=${code}&state=${state}`).then(
        function (response) {
          console.log(response);
          if(response.data) {
            let token = response.data.access_token
            let expires_in = response.data.expires_in
            let token_type = response.data.token_type
            localStorage.setItem('access_token', token);
            localStorage.setItem('expires_in', expires_in);
            localStorage.setItem('token_type', token_type);
          }
        }
        ).catch(function (error) {
          console.log(error);
        })
    },

    urlSearch: function (key){
      let name,value,str=location.href,num=str.indexOf("?"); //取得整个地址栏
      str=str.substr(num+1); //取得所有参数 stringvar.substr(start [, length ]
      let arr=str.split("&"); //各个参数放到数组里
      console.log(arr)
      for(let i=0;i < arr.length;i++){
        num=arr[i].indexOf("=");
        if(num>0){
          name=arr[i].substring(0,num);
          value=arr[i].substr(num+1);
          // this[name]=value;
          if(name === key) {
            return value
          }
        }
      }
    }

  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
