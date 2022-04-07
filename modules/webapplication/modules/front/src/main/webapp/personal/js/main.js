  function sys(){
   if(navigator.appName=='Microsoft Internet Explorer'){
      document.getElementById("sysmsg").innerHTML='您使用的浏览器是微软公司的IE浏览器<br />';
   }
       document.getElementById("sysmsg").innerHTML+='您的屏幕分辨率为'+screen.width+'×'+screen.height;
  }
  function addFav(title) {
    var url=location.href;
    if (window.sidebar) { 
        window.sidebar.addPanel(title, url,""); 
       } else if( document.all ) {
        window.external.AddFavorite( url, title);
       } else if( window.opera && window.print ) {
        return true;
      }
   }
  window.onload=sys;