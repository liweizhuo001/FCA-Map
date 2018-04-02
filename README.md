# FCA-Map
Copyright 2017 by  Academy of Mathematics and Systems Science Chinese Academy of Sciences

FCA_Map is a tool for ontology matching, which uses the Formal Concept Analysis formalism to cluster the commonalities across ontologies.

Time: 12/26/2017  Author: Mengyi Zhao & Weizhuo Li   Mail:  liweizhuo@amss.ac.cn & zhaomengyi14@mails.ucas.ac.cn

Software: Java 1.7 or higher.   
Hardware: 16GB RAM or more. The CPU is not limited, but we still hope that the CPU in your computer is as efficient as possible, which can reduce a lot of time consumption.

Usage:   
At present, we mainly provide the interface for IDE such as Eclipse. If you install it, you can import this project directly.

a)download the Lexical_Tools.7z in the link https://pan.baidu.com/s/1i4SKy4t password: sxdq . Unzip the files Lexical_Tools.7z in the project directory.

b)run MatcherBridge1.java in the package edu.amss.fca.Method for matching tasks. In this class, we provide all the paths of the matching tasks. 

c)run StatisticResult.java in the package edu.amss.fca.Method to evaluate results.

Data sets:  
We just provide some simple data sets and their reference alignments such as Conference Track and Anatomy Track for your testing. The others about Phenotype Track and Largebio Track should be downloaded in Ontology Alignment Evaluation Initiative (OAEI). 

Results:  
The file alignment contains all the results of four tracks.

More details for Reading:  
[1] Zhao M, Zhang S.  "Identifying and validating ontology mappings by formal concept analysis" available at:   
http://disi.unitn.it/~pavel/om2016/papers/om2016_Tpaper6.pdf
[2] Zhao M, Zhang S.  "FCA-Map results for OAEI 2016"  available at:   
http://disi.unitn.it/~pavel/om2016/papers/oaei16_paper7.pdf
[3] Zhao, M., Zhang, S., Li, W., & Chen, G. "Matching biomedical ontologies based on formal concept analysis"  available at:  
https://link.springer.com/article/10.1186/s13326-018-0178-9
