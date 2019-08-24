# 安装
官方链接：https://github.com/tensorflow/serving  

## 安装方法
官方给出很多安装办法，这里首先给出试验后的建议方法  

建议方案：
* 如果是测试，请直接使用 Docker 安装，方便快捷
* 如果是上线，请使用 Docker 编译，环境隔离，无冲突，耗时长

不建议：
* 直接在Linux(非Docker)上编译，容易产生各种错误和冲突问题

不论采用什么方法，serving的核心执行文件是 tensorflow_model_server  
不论是否在Docker上，最终都是执行 tensorflow_model_server 命令来启动serving进程  
而如果是在Docker上，不过是将 tensorflow_model_server 安装在了Docker镜像中，通过docker指定镜像来启动serving进程  

## docker的安装
docker官方很详细，直接参考：https://docs.docker.com/install/linux/docker-ce/centos/  

这里直接给出我在centOS中的安装命令
```bash
sudo yum install -y yum-utils \
device-mapper-persistent-data \
lvm2

# 设置repository
sudo yum-config-manager \
--add-repo \
https://download.docker.com/linux/centos/docker-ce.repo

# 安装最新的Docker
sudo yum install docker-ce docker-ce-cli containerd.io

# 启动Docker
sudo systemctl start docker

# 检查是否安装成功
sudo docker run hello-world
```

## 用Docker快速安装Tensorflow Serving
很简单了，一条命令就搞定
```bash
docker pull tensorflow/serving
```
拉取tensorflow/serving的docker镜像，然后就可以直接使用了  
  
我们测试一下  
```bash
# 拉取源码，主要是为了获得demo模型
git clone https://github.com/tensorflow/serving

# Location of demo models
TESTDATA="$(pwd)/serving/tensorflow_serving/servables/tensorflow/testdata"

# Start TensorFlow Serving container and open the REST API port
docker run -t --rm -p 8501:8501 \
    -v "$TESTDATA/saved_model_half_plus_two_cpu:/models/half_plus_two" \
    -e MODEL_NAME=half_plus_two \
    tensorflow/serving &

# 或者执行如下命令
docker run -t -p 8501:8501 \
    --mount type=bind,source=$TESTDATA/saved_model_half_plus_two_cpu,target=/models/half_plus_two \
    -e MODEL_NAME=half_plus_two tensorflow/serving:latest

# Query the model using the predict API
curl -d '{"instances": [1.0, 2.0, 5.0]}' \
    -X POST http://localhost:8501/v1/models/half_plus_two:predict

# Returns => { "predictions": [2.5, 3.0, 4.5] }

```
上面，docker run 的两条命令效果是一样的，都是在docker中启动镜像tensorflow/serving，镜像的版本都是latest  
-p 8501:8501 是指定暴露的端口，其中左边启动docker的宿主机的端口，右边是要绑定的容器暴露的端口  
由于 tensorflow_model_server 默认会在8500启动 gRPC服务，在8501启动RestAPI服务  
因此，上面的命令是将宿主机的8501绑定到了RestAPI接口, 没有绑定gRPC的接口，因此无法通过gRPC调用，需要则增加参数-p 8500:8500即可  
随后，便可发送http请求  


#### 使用开发版
上面拉取的是正式版，不可修改，我们还可以用开发版，定制镜像
```bash
docker pull tensorflow/serving:latest-devel

# 用开发版镜像启动容器，之后便可使用命令tensorflow_model_server
docker run -it -p 8500:8500 tensorflow/serving:latest-devel  

# 启动另一个终端
docker ps  # 查看container id
# 复制指定模型到container
docker cp $TESTDATA/saved_model_half_plus_two_cpu  5d62364f2186:/online_model  

# 回到启动容器的终端, 启动server
tensorflow_model_server --port=8500 --rest_api_port=8501 --model_name=dev_model --model_base_path=/online_model  
```

用上面的方法，在启动时，会发现下面这条日志：  
> Your CPU supports instructions that this TensorFlow binary was not compiled to use: AVX2 FMA  

说明，安装的Tensorflow Serving没有支持 AVX2 和 FMA 指令，因此无法充分利用CPU的能力，需要自行编译安装
   
编译的方式有两种：
* 在Docker上编译
* 在Linux上编译

## 用Docker编译安装Tensorflow serving
用Docker编译安装是比较推荐的方法，复用更加方便，而且不会产生与Linux上包冲突的问题  
直接看脚本  
```bash
# 建议指定版本，与tensorflow版本一致
TF_SERVING_VERSION_GIT_BRANCH="r1.13"
git clone --branch="${TF_SERVING_VERSION_GIT_BRANCH}" https://github.com/tensorflow/serving
cd serving

docker build --pull -t $USER/tensorflow-serving-devel -f tensorflow_serving/tools/docker/Dockerfile.devel .

docker build -t $USER/tensorflow-serving \
  --build-arg TF_SERVING_BUILD_IMAGE=$USER/tensorflow-serving-devel \
  -f tensorflow_serving/tools/docker/Dockerfile .

# 完成后执行 docker image ls
# 可以见到如下两行，说明镜像创建成功
# root/tensorflow-serving         latest              362c29280cd2        13 seconds ago      235MB
# root/tensorflow-serving-devel   latest              c5e9090b6e45        2 hours ago         3.87GB

# 启动server
docker run -t -p 8501:8501 \
    --mount type=bind,source=$TESTDATA/saved_model_half_plus_two_cpu,target=/models/half_plus_two \ 
    -e MODEL_NAME=half_plus_two root/tensorflow-serving:latest
```

或者自定义参数安装：
```bash
USER=$1
TAG=$2
TF_SERVING_VERSION_GIT_BRANCH="r1.13"
git clone --branch="${TF_SERVING_VERSION_GIT_BRANCH}" https://github.com/tensorflow/serving

TF_SERVING_BUILD_OPTIONS="--copt=-mavx2 --copt=-mfma"
cd serving && \
  docker build --pull -t $USER/tensorflow-serving-devel:$TAG \
  --build-arg TF_SERVING_VERSION_GIT_BRANCH="${TF_SERVING_VERSION_GIT_BRANCH}" \
  --build-arg TF_SERVING_BUILD_OPTIONS="${TF_SERVING_BUILD_OPTIONS}" \
  -f tensorflow_serving/tools/docker/Dockerfile.devel .

cd serving && \
  docker build -t $USER/tensorflow-serving:$TAG \
  --build-arg TF_SERVING_BUILD_IMAGE=$USER/tensorflow-serving-devel:$TAG \
  -f tensorflow_serving/tools/docker/Dockerfile .
```

## Linux编译安装Tensorflow Seving
参考：https://github.com/tensorflow/serving/blob/1.13.0/tensorflow_serving/g3doc/setup.md

命令也很简单，但是有可能会报错  
```bash
# 下载指定版本的源码
git clone -b r1.13 https://github.com/tensorflow/serving.git
cd serving

tools/run_in_docker.sh bazel build --copt=-mavx2 --copt=-mfma tensorflow_serving/...
# 如果有下面报错，按提示添加参数即可 --incompatible_disallow_filetype=false --incompatible_disallow_data_transition=false 

```

报错1：
```text
ERROR: /disk1/gaoqing/tensorflow_serving/serving/.cache/_bazel_root/a03072540d2e5d5fb519b5992c33970b/external/io_bazel_rules_closure/closure/private/defs.bzl:18:17: FileType function is not available. You may use a list of strings instead. You can temporarily reenable the function by passing the flag --incompatible_disallow_filetype=false
```

报错2：
```text
ERROR: /disk1/gaoqing/tensorflow_serving/serving/.cache/_bazel_root/a03072540d2e5d5fb519b5992c33970b/external/io_bazel_rules_closure/closure/testing/phantomjs_test.bzl:70:17: Traceback (most recent call last):
 File "/disk1/gaoqing/tensorflow_serving/serving/.cache/_bazel_root/a03072540d2e5d5fb519b5992c33970b/external/io_bazel_rules_closure/closure/testing/phantomjs_test.bzl", line 57
  rule(test = True, implementation = _imp..., ...")})
 File "/disk1/gaoqing/tensorflow_serving/serving/.cache/_bazel_root/a03072540d2e5d5fb519b5992c33970b/external/io_bazel_rules_closure/closure/testing/phantomjs_test.bzl", line 70, in rule
  attr.label_list(cfg = "data", allow_files = True)
Using cfg = "data" on an attribute is a noop and no longer supported. Please remove it. You can use --incompatible_disallow_data_transition=false to temporarily disable this check.
```

编译完成后，运行 bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server --help  报错如下：

```text
bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.20' not found (required by bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server)
bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server: /lib64/libstdc++.so.6: version `CXXABI_1.3.8' not found (required by bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server)
bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.21' not found (required by bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server)
bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server: /lib64/libstdc++.so.6: version `CXXABI_1.3.11' not found (required by bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server)
bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.22' not found (required by bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server)
bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server: /lib64/libm.so.6: version `GLIBC_2.27' not found (required by bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server)
bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server: /lib64/libm.so.6: version `GLIBC_2.23' not found (required by bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server)
```

说明 gcc 和 glibc 两个包发生冲突了  
但是一般情况下，安装了tensorflow，系统中就存在新版本的 gcc 和 glibc，可以直接找出来替换之  

##### 查找和替换软连接
```bash
find / -name "libstdc++.so.*"
# 发现有 /root/anaconda3/lib/libstdc++.so.6.0.25，因为在安装TensorFlow时会生成新的动态库
# 然后复制和替换相应的软件连接
cd /root/anaconda3/lib/
cp libstdc++.so.6.0.25 /usr/local/lib64/
cp /usr/local/lib64/libstdc++.so.6.0.25 /lib64
cd /lib64
rm -rf libstdc++.so.6
ln -s libstdc++.so.6.0.25 libstdc++.so.6
# 再次执行 bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server --help 

# 就只剩下 libm.so.6 的报错了, 一样找找看，也可以前往http://www.gnu.org/software/libc/ 下载最新版本安装
find / -name "libm-2.27.so"
cp libm-2.27.so /usr/local/lib64/
cp /usr/local/lib64/libm-2.27.so /usr/lib64
cd /usr/lib64
rm -rf libm.so.6
ln -s libm-2.27.so libm.so.6
# 再次尝试，成功！！
```


如果没有找到 libstdc++.so.6.0.25，则更新gcc  
```bash
yum groupinstall "Development Tools"
yum install glibc-static libstdc++-static

# http://ftp.gnu.org/gnu/gcc/
wget http://ftp.gnu.org/gnu/gcc/gcc-6.4.0/gcc-6.4.0.tar.gz
tar -zxvf gcc-6.4.0.tar.gz 

cd gcc-6.4.0
./contrib/download_prerequisites

mkdir build
cd build
../configure -enable-checking=release -enable-languages=c,c++ -disable-multilib
make -j4
make install

# 重启Linux
cp /usr/local/lib64/libstdc++.so.6.0.22 /lib64
cd /lib64
rm -rf libstdc++.so.6
ln -s libstdc++.so.6.0.22 libstdc++.so.6
```

如果没有找到 libm-2.27.so，也可以更新安装  


##### 测试
```bash
serving/bazel-bin/tensorflow_serving/model_servers/tensorflow_model_server --port=8500 --rest_api_port=8501 --enable_batching=true --model_name=test_model --model_base_path=$TESTDATA/saved_model_half_plus_two_cpu

curl -d '{"instances": [1.0, 2.0, 5.0]}'     -X POST http://localhost:8501/v1/models/test_model:predict
```
