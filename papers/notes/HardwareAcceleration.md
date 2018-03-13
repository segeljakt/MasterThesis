# Hardware Accelerated Convolutional Neural Networks for Synthetic Vision Systems

[@ThisPaper] presents a hardware archidata-flow engine capable large scale convolutional of analysing mega-pixel sized images in real-time. It uses 















Deep learning is 

Convolutional neural networks (CNNs) are a kind of neural networks suited to image processing [@ThisPaper]. General use cases of CNNs are image detection, recognition or localization. Images are generally represented in memory as matrices or tensors. This data locality is exploited by CNNs to decrease the number of weights, or parameters, needed for learning.

In the forward pass, the CNN passes its input through a set of layers. Each layer applies an operation to the input, and forwards the output to the next layer. Layers can be of different types, e.g., convolutional, pooling, ReLU, fully connected, or loss. The cornerstone is however the convolutional layer. The convolutional layer slides over the input matrix while computing the dot product with a filter to produce an output activation map.

Affine transformations and convolutional operations both map well onto GPUs.


# Scaling up and Scaling out

There are two general approaches to scaling a data parallel processing architecture: scaling up and scaling out. Scaling up involves modifying existing machines to make them more powerful. This can be expensive, but also effective as it is almost guaranteed to improve performance. Scaling out instead adds new machines to the network rather than modifying existing ones. It is cheaper since the new machines do not necessarily have to be more powerful. The downside is the increase in network communication and management costs for each machine added to the network.

[@Scale-up x Scale-out: A Case Study using Nutch/Lucene]


is cheaperScaling out is the opposite. It involves It buying more machines to increase the parallelism. This comes at the cost of money, since 






. There are multiple types of layers: Convolutional layer, pooling layer, , but the most important type is the convolutional layer. Convolution operations can either be 

In the feed-forward phase, where prediction occurs, the CNN passes an input through each layer. The feedforward phase of CNNs applies convolution operations to the input data. Convolution operations take a learn through convolution operations. have a set of convolution operations 

Deep neural networks (DNNs) are 
Deep neural networks (DNNs) are 
