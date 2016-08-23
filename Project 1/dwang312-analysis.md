---
output: html_document
---
ASSIGNMENT 1 – SUPERVISED LEARNING
Di Wang
902915079

Reference

http://archive.ics.uci.edu/ml/datasets/Concrete+Compressive+Strength
http://archive.ics.uci.edu/ml/datasets/Congressional+Voting+Records
http://www.euclidean.com/machine-learning-in-practice/2015/6/12/r-caret-and-parameter-tuning-c50
https://cran.r-project.org/web/packages/kernlab/kernlab.pdf


```{r echo=FALSE, warning=FALSE, message=FALSE}

#install.packages("C50")
#install.packages("gmodels")
#install.packages("caret")
#install.packages("e1071")
#install.packages("plyr")
#install.packages("corrplot")
#install.packages("mlbench")
#install.packages("nnet")
#install.packages("kernlab")
#install.packages("class")



library(caret)
library(e1071)
library(C50)
library(gmodels)
library(corrplot)
library(plyr)
library(mlbench)
library(nnet)
library(kernlab)
library(class)

```

# Read Data
```{r}
ConcreteData <- read.csv("ConcreteData.csv")
Voting <- read.csv("Congressional Voting Records Data Set.csv")

```
# To see the natural of the data

M1 <- cor(ConcreteData)
M2 <- cor(Voting)

corrplot(M1,tl.pos='n',method = "circle")
corrplot(M2, tl.pos='n',method = "circle")

#Set aside 20% sample data
```{r}
set.seed(1234)
Concrete_rand <- ConcreteData[order(runif(nrow(ConcreteData))), ]
length_c <- 0.8*nrow(Concrete_rand)
c_tree_train <- Concrete_rand[1:length_c, ]
c_tree_test  <- Concrete_rand[length_c:nrow(ConcreteData), ]

set.seed(1234)
Voting_rand <- Voting[order(runif(nrow(Voting))), ]
length_v <- round(0.8*nrow(Voting))
v_tree_train <- Voting[1:length_v, ]
v_tree_test  <- Voting[length_v:nrow(Voting), ]

```


# Decision Tree

#Building the classifier

```{r}
#Decision Tree without pruning
#Set 1

c_tree_model <- C5.0(c_tree_train[-9], c_tree_train$Classification,trail=1)
v_tree_model <- C5.0(v_tree_train[-1], v_tree_train$Class,trail=1)

c_tree_predict <- predict(c_tree_model, c_tree_test, type = "class")
c_tree_predict2 <- predict(c_tree_model, c_tree_train, type = "class")
v_tree_predict <- predict(v_tree_model, v_tree_test, type = "class")
v_tree_predict2 <- predict(v_tree_model, v_tree_train, type = "class")

postResample(c_tree_predict, c_tree_test$Classification)
postResample(c_tree_predict2, c_tree_train$Classification)
postResample(v_tree_predict, v_tree_test$Class)
postResample(v_tree_predict2, v_tree_train$Class)

#Set 2
c_tree_model <- C5.0(c_tree_train[-9], c_tree_train$Classification,trail=1,control = C5.0Control(minCases = 5))
v_tree_model <- C5.0(v_tree_train[-1], v_tree_train$Class,trail=1,control = C5.0Control(minCases = 5))

c_tree_predict <- predict(c_tree_model, c_tree_test, type = "class")
c_tree_predict2 <- predict(c_tree_model, c_tree_train, type = "class")
v_tree_predict <- predict(v_tree_model, v_tree_test, type = "class")
v_tree_predict2 <- predict(v_tree_model, v_tree_train, type = "class")

postResample(c_tree_predict, c_tree_test$Classification)
postResample(c_tree_predict2, c_tree_train$Classification)
postResample(v_tree_predict, v_tree_test$Class)
postResample(v_tree_predict2, v_tree_train$Class)

#Pruning the decision tree
#Set 3
c_tree_model <- C5.0(c_tree_train[-9], c_tree_train$Classification,trail=1,control = C5.0Control(noGlobalPruning = FALSE,CF=0.1))
v_tree_model <- C5.0(v_tree_train[-1], v_tree_train$Class,trail=1,control = C5.0Control(noGlobalPruning = FALSE,CF=0.1))

c_tree_predict <- predict(c_tree_model, c_tree_test, type = "class")
c_tree_predict2 <- predict(c_tree_model, c_tree_train, type = "class")
v_tree_predict <- predict(v_tree_model, v_tree_test, type = "class")
v_tree_predict2 <- predict(v_tree_model, v_tree_train, type = "class")

postResample(c_tree_predict, c_tree_test$Classification)
postResample(c_tree_predict2, c_tree_train$Classification)
postResample(v_tree_predict, v_tree_test$Class)
postResample(v_tree_predict2, v_tree_train$Class)

#Set 4
c_tree_model <- C5.0(c_tree_train[-9], c_tree_train$Classification,trail=1,control = C5.0Control(noGlobalPruning = FALSE,CF=0.2))
v_tree_model <- C5.0(v_tree_train[-1], v_tree_train$Class,trail=1,control = C5.0Control(noGlobalPruning = FALSE,CF=0.2))

c_tree_predict <- predict(c_tree_model, c_tree_test, type = "class")
c_tree_predict2 <- predict(c_tree_model, c_tree_train, type = "class")
v_tree_predict <- predict(v_tree_model, v_tree_test, type = "class")
v_tree_predict2 <- predict(v_tree_model, v_tree_train, type = "class")

postResample(c_tree_predict, c_tree_test$Classification)
postResample(c_tree_predict2, c_tree_train$Classification)
postResample(v_tree_predict, v_tree_test$Class)
postResample(v_tree_predict2, v_tree_train$Class)
```

#Evaluating the boosting performance
```{r echo=FALSE, warning=FALSE, message=FALSE}
#10-folded Crossvalidation, 10 repeats

fitControl <- trainControl(method = "repeatedcv",
                           number = 10,
                           repeats = 10, returnResamp="all")

grid <- expand.grid( .winnow = c(TRUE,FALSE), .trials=c(1,5,10,15,20), .model="tree" )

#Concrete
mdl<- train(c_tree_train[-9],y=c_tree_train$Classification,tuneGrid=grid,trControl=fitControl,method="C5.0",verbose=FALSE)
plot(mdl)

#Voting
mdl2<- train(x=v_tree_train[-1],y=v_tree_train$Class,tuneGrid=grid,trControl=fitControl,method="C5.0",verbose=FALSE)
plot(mdl2)

#Test
#Concrete
c_tree_model <- C5.0(c_tree_train[-9], c_tree_train$Classification,trail=20,winnow=FALSE)
c_tree_predict <- predict(c_tree_model, c_tree_test, type = "class")
postResample(c_tree_predict, c_tree_test$Classification)

#Voting

v_tree_model <- C5.0(v_tree_train[-1], v_tree_train$Class,trail=20, winnow=FALSE)
V_tree_predict <- predict(V_tree_model, V_tree_test, type = "class")
postResample(v_tree_predict, v_tree_test$Class)

```


#Neural Network
```{r}
#Nnet
#The size argument specifies how many nodes to have in the hidden layer

#K-fold iteration

# 10-folded Crossvalidation, 3 repeats

fitControl <- trainControl(method = "repeatedcv",
                           number = 10,
                           repeats = 5, returnResamp="all")

my.grid <- expand.grid(.decay = c(0.1,0.2,0.3,0.4), .size = c(1,5,8,10,15))

#Concrete
prestige.fit <- train(c_tree_train$Classification ~.,
                      data=c_tree_train[-9],
                      trControl=fitControl,
                      method ="nnet", 
                      maxit = 500, 
                      tuneGrid = my.grid, 
                      trace = F,
                      linout = 1)

plot(prestige.fit)

#Voting
prestige.fit2 <- train(v_tree_train$Class~ .,
                       data=v_tree_train[-1],
                       method = "nnet", 
                       trControl=fitControl,
                       tuneGrid = my.grid,
                       maxit = 500)
plot(prestige.fit2)


#Set 1 Concrete
c_net_model <- nnet(c_tree_train$Classification ~ .,data=c_tree_train[-9], size = 15,decay=0.4,maxit = 500)
c_net_predict <- predict(c_net_model, c_tree_test, type = "class")
postResample(c_net_predict, c_tree_test$Classification)


#Set 2 Voting
v_net_model <- nnet(v_tree_train$Class ~ .,data=v_tree_train[-1], size = 5,decay=0.4,maxit = 500)
v_net_predict <- predict(v_net_model, v_tree_test, type = "class")
postResample(v_net_predict, v_tree_test$Class)

```

#Support Vector Machine
```{r}
#Train Function
my.grid <- expand.grid(.degree = c(1,2,3,4,5,7,10), .C = c(0.1,0.25,0.5,1), .scale = 0.1)

#Concrete
prestige.fit.svm <- train(c_tree_train$Classification ~.,
                      data=c_tree_train[-9],
                      tuneGrid = my.grid,
                      trControl=fitControl,
                      method ="svmPoly")

plot(prestige.fit.svm)

#Voting
prestige.fit.svm2 <- train(v_tree_train$Class~ .,
                       data=v_tree_train[-1],
                       method = "svmPoly", 
                       trControl=fitControl,
                       tuneGrid = my.grid)

plot(prestige.fit.svm2)

# Testing
#C
c_vsm_model <- ksvm(c_tree_train$Classification ~ .,data=c_tree_train[-9],
                           kernel = "polydot", kpar=list(degree=7,scale=1))
c_net_predict <- predict(c_vsm_model, c_tree_test)
postResample(c_net_predict, c_tree_test$Classification)

#V
v_vsm_model <- ksvm(v_tree_train$Class ~ .,data=v_tree_train[-1],
                           kernel = "polydot", kpar=list(degree=2,scale=0.25))
v_net_predict <- predict(v_vsm_model, v_tree_test)
postResample(v_net_predict, v_tree_test$Class)

#Radial Basis Gaussian
my.grid <- expand.grid(.sigma=c(0.05,0.1,0.15,0.2), .C = c(0.1,0.25,0.5,1))

#svmRadial
#c
prestige.fit.svm3 <- train(c_tree_train$Classification ~.,
                      data=c_tree_train[-9],
                      tuneGrid = my.grid,
                      trControl=fitControl,
                      method ="svmRadial")

plot(prestige.fit.svm3)

#V
prestige.fit.svm4 <- train(v_tree_train$Class ~.,
                      data=v_tree_train[-1],
                      tuneGrid = my.grid,
                      trControl=fitControl,
                      method ="svmRadial")

plot(prestige.fit.svm4)

#Testing
#C
c_vsm_model3 <- ksvm(c_tree_train$Classification ~ .,data=c_tree_train[-9],
                           kernel = "rbfdot", kpar=list(sigma=0.2))
c_net_predict3 <- predict(c_vsm_model3, c_tree_test)
postResample(c_net_predict3, c_tree_test$Classification)

#V
v_vsm_model4 <- ksvm(v_tree_train$Class ~ .,data=v_tree_train[-1],kernel = "rbfdot", kpar=list(sigma=0.05))

v_net_predict4 <- predict(v_vsm_model4, v_tree_test)
postResample(v_net_predict4, v_tree_test$Class)

#tanhdot
#concrete
c_vsm_model <- ksvm(c_tree_train$Classification ~ .,data=c_tree_train[-9],
                            kernel = "tanhdot")
c_net_predict <- predict(c_vsm_model, c_tree_test, type = "response")
postResample(c_net_predict, c_tree_test$Classification)

#Voting
v_vsm_model <- ksvm(v_tree_train$Class ~ .,data=v_tree_train[-1],
                            kernel = "tanhdot")
v_net_predict <- predict(v_vsm_model, v_tree_test, type = "response")
postResample(v_net_predict, v_tree_test$Class)

#anovadot
#concrete
c_vsm_model <- ksvm(c_tree_train$Classification ~ .,data=c_tree_train[-9],
                            kernel = "anovadot")
c_net_predict <- predict(c_vsm_model, c_tree_test, type = "response")
postResample(c_net_predict, c_tree_test$Classification)

#Voting
v_vsm_model <- ksvm(v_tree_train$Class ~ .,data=v_tree_train[-1],
                            kernel = "anovadot")
v_net_predict <- predict(v_vsm_model, v_tree_test, type = "response")
postResample(v_net_predict, v_tree_test$Class)

#laplacedot
#Concrete
c_vsm_model <- ksvm(c_tree_train$Classification ~ .,data=c_tree_train[-9],
                            kernel = "laplacedot")
c_net_predict <- predict(c_vsm_model, c_tree_test, type = "response")
postResample(c_net_predict, c_tree_test$Classification)

#Voting
v_vsm_model <- ksvm(v_tree_train$Class ~ .,data=v_tree_train[-1],
                            kernel = "laplacedot")
v_net_predict <- predict(v_vsm_model, v_tree_test, type = "response")
postResample(v_net_predict, v_tree_test$Class)
```

#K-Nearest Neighboor
```{r}

#Voting-----KNN cannot used for categorical data/ Create Dummy Variables
v_knn <- v_tree_train
v_knn_test <- v_tree_test

for (i in 2:length(v_knn)){
  v_knn[,i] <- as.numeric(v_knn[,i])
}

for (i in 2:length(v_knn_test)){
  v_knn_test[,i] <- as.numeric(v_knn_test[,i])
}


#####
my.grid <- expand.grid(.k=c(1,2,3,4,5,6,7,8,10,20,50))

#svmRadial
#c
prestige.fit.knn <- train(c_tree_train$Classification ~.,
                      data=c_tree_train[-9],
                      tuneGrid = my.grid,
                      trControl=fitControl,
                      method ="knn")

plot(prestige.fit.knn)

#V
prestige.fit.knn2 <- train(v_knn$Class ~.,
                      data=v_knn[-1],
                      tuneGrid = my.grid,
                      trControl=fitControl,
                      method ="knn")

plot(prestige.fit.knn2)

#Concrete
c_knn <- knn(train = c_tree_train[,-9], test = c_tree_test[,-9], cl=c_tree_train[,9],k=1)
postResample(c_knn,c_tree_test$Classification)

#Voting
v_knn_p <- knn(train =v_knn[,-1], test = v_knn_test[,-1], cl=v_knn[,1],k=4)
postResample(v_knn_p,v_knn_test$Class)


  
names(v_knn)[1] <- paste("classcol")
names(v_knn_test)[1] <- paste("classcol")

v_knn_model <- knncat (train = v_knn, k=c(1,2,3,4,5))
plot(v_knn_model)
v_knn_p <- predict(v_knn_model, v_knn_test[-1])

postResample(v_knn_p,v_knn_test$classcol)
```

#Time Spent
```{r}
t_matrix <- matrix(c(0.04,0.03,1.14,0.09,0,0.01,0.04,5.54,0.04,0,"Decison tree","Boosting"))
# Use weka as a support tool

```

#Learning Curve
```{r}

set.seed(1234)
C_learn <- ConcreteData[order(runif(nrow(ConcreteData))), ]
l_c_learn <- nrow(C_learn)

#Split out 20% Testing data
len <- floor(0.8*l_c_learn)
c_curve_test  <- C_learn[len:l_c_learn, ]
c_curve_train <- C_learn[1:len, ]


c <- matrix(nrow=20,ncol=3)

for (i in 1:10){
  l_c_train <- nrow(c_curve_train)
  len_train <- floor(0.1*i*l_c_learn)
  c_curve_train_part <- c_curve_train[1:len_train, ]
 
  c_curve_model <- C5.0(c_curve_train_part[-9], c_curve_train_part$Classification)
  
  c_curve_predict <- predict(c_curve_model, c_curve_test, type = "class")
  c_curve_sample <- predict(c_curve_model,c_curve_train,type="class")
  
  pb_test <- postResample(c_curve_predict, c_curve_test$Classification)
  pb_train <- postResample(c_curve_sample, c_curve_train$Classification)
  
  c[i,1]<-round(pb_test[1],4)
  c[10+i,1]<-round(pb_train[1],4)
  c[i,2]<-0.1*i
  c[10+i,2]<-0.1*i
  c[i,3]<-"Prediction"
  c[10+i,3]<-"Train"
  
}

c <- as.data.frame(c)
ggplot(data=c,aes(x=V2,y=V1,color=V3,group=factor(V3))) + geom_line() + 
  xlab("Samples Used") +ylab("Accuracy") + 
  ggtitle("Sample Size vs Accuracy(Learning Curve for C5.0)") 

#For voting Learning Curve

set.seed(1234)
C_learn <- Voting[order(runif(nrow(Voting))), ]
l_c_learn <- nrow(C_learn)

#Split out 20% Testing data
len <- floor(0.8*l_c_learn)
c_curve_test  <- C_learn[len:l_c_learn, ]
c_curve_train <- C_learn[1:len, ]


c <- matrix(nrow=20,ncol=3)

for (i in 1:10){
  l_c_train <- nrow(c_curve_train)
  len_train <- floor(0.1*i*l_c_learn)
  c_curve_train_part <- c_curve_train[1:len_train, ]
 
  c_curve_model <- C5.0(c_curve_train_part[-1], c_curve_train_part$Class)
  
  c_curve_predict <- predict(c_curve_model, c_curve_test, type = "class")
  c_curve_sample <- predict(c_curve_model,c_curve_train,type="class")
  
  pb_test <- postResample(c_curve_predict, c_curve_test$Class)
  pb_train <- postResample(c_curve_sample, c_curve_train$Class)
  
  c[i,1]<-round(pb_test[1],4)
  c[10+i,1]<-round(pb_train[1],4)
  c[i,2]<-0.1*i
  c[10+i,2]<-0.1*i
  c[i,3]<-"Prediction"
  c[10+i,3]<-"Train"
  
}

c <- as.data.frame(c)
ggplot(data=c,aes(x=V2,y=V1,color=V3,group=factor(V3))) + geom_line() + xlab("Samples Used") +ylab("Accuracy") + ggtitle("Sample Size vs Accuracy(Learning Curve for C5.0)")
```


