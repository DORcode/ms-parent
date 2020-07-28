<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <p>
      For a guide and recipes on how to configure / customize this project,<br>
      check out the
      <a href="https://cli.vuejs.org" target="_blank" rel="noopener">vue-cli documentation</a>.
    </p>
    <h3>Installed CLI Plugins</h3>
    <ul>
      <li><a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-babel" target="_blank" rel="noopener">babel</a></li>
      <li><a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-eslint" target="_blank" rel="noopener">eslint</a></li>
    </ul>
    <h3>Essential Links</h3>
    <ul>
      <li><a href="https://vuejs.org" target="_blank" rel="noopener">Core Docs</a></li>
      <li><a href="https://forum.vuejs.org" target="_blank" rel="noopener">Forum</a></li>
      <li><a href="https://chat.vuejs.org" target="_blank" rel="noopener">Community Chat</a></li>
      <li><a href="https://twitter.com/vuejs" target="_blank" rel="noopener">Twitter</a></li>
      <li><a href="https://news.vuejs.org" target="_blank" rel="noopener">News</a></li>
    </ul>
    <h3>Ecosystem</h3>
    <ul>
      <li><a href="https://router.vuejs.org" target="_blank" rel="noopener">vue-router</a></li>
      <li><a href="https://vuex.vuejs.org" target="_blank" rel="noopener">vuex</a></li>
      <li><a href="https://github.com/vuejs/vue-devtools#vue-devtools" target="_blank" rel="noopener">vue-devtools</a></li>
      <li><a href="https://vue-loader.vuejs.org" target="_blank" rel="noopener">vue-loader</a></li>
      <li><a href="https://github.com/vuejs/awesome-vue" target="_blank" rel="noopener">awesome-vue</a></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },

  data: function () {
    // Authorization Bearer
    // this.test_seperate_sso();

    const a = 'http://localhost:8090/oauth/authorize?client_id=ms-client&redirect_uri=http://localhost:8281/client/login&response_type=code';
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

    accessToken: function () {

      let code = this.urlSearch('code')
      let state = this.urlSearch('state')
      // 如果存在 token，并且没有过期，
      if (code && !localStorage.getItem('access_token')) {
        console.log(code);
        this.accessToken(code, state);
      } else if(!code && !localStorage.getItem('access_token')) {
        const a = 'http://localhost:8090/oauth/authorize?client_id=ms-client&redirect_uri=http://localhost:8281/client/login&response_type=code';

        window.location.href = a
        // window.open(a, '_blank');
      } else if(!code) {
        let token = localStorage.getItem('access_token')
        let type = localStorage.getItem('token_type')

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
      }
      
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
