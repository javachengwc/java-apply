此目录别删
默认情况下springboot中request.getServletContext().getRealPath("/") 返回的是一个临时文件夹的地址
在springboot项目resource目录下创建public或者static的文件夹，那么通过 request.getServletContext().getRealPath("/")会得到public或者static的路径