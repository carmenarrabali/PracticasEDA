# Reading the data
#rawdata = read.table("stats_latinsquare10batch.txt");
rawdata = read.table("stats_ls10.txt");
# Compute basic statistics 
m <- dim(rawdata)[1];
n <- dim(rawdata)[2];
even_ind <- seq(2, n, 2)
odd_ind  <- seq(3, n, 2)
stderr <- function(x) sd(x)/sqrt(length(x))
dataframe <- data.frame(
  "clues" = rawdata[,1],
  "nodes" = apply(t(rawdata)[even_ind,], 2, mean),
  "nodeserr" = apply(t(rawdata)[even_ind,], 2, stderr), 
  "solvable" = apply(t(rawdata)[odd_ind,], 2, mean),
  "solvableerr" = apply(t(rawdata)[odd_ind,], 2, stderr)
  )

# Find the range of clues that encloses 50% solvavbility
mini <- min(which(dataframe$solvable<.5, arr.ind=TRUE))-1
maxi <- max(which(dataframe$solvable>.5, arr.ind=TRUE))+1
psol1 <- dataframe$clues[mini]
psol2 <- dataframe$clues[maxi]

maxnodesi <- which.max(dataframe$nodes)
pnodes <- dataframe$clues[maxnodesi]

# Graphical representation

library(ggplot2)
figure_nodes <- ggplot(data=dataframe, aes(x=clues, y=nodes)) + 
  geom_line(colour = "blue") +
  geom_errorbar(aes(ymin=nodes-nodeserr, ymax=nodes+nodeserr), colour="blue", width=.02) +
  geom_point(shape=21, size=1, colour="blue",  fill="white") +
  xlab ("fraction of pre-assigned elements") +
  ylab ("nodes expanded") +
  geom_line(data=data.frame(x=c(pnodes, pnodes), y = c(0, +Inf)), aes(x=x, y=y), colour = "black", size = .5, linetype = "dashed") + 
  geom_text(aes(x = pnodes, y = 0, label = format(pnodes), vjust = "top"), colour = "black") + 
  theme_bw() 

show(figure_nodes)
ggsave("nodes.png", plot = figure_nodes, device = "png")

figure_solvable <- ggplot(data=dataframe, aes(x=clues, y=solvable)) + 
  geom_line(colour = "blue") +
  geom_errorbar(aes(ymin=solvable-solvableerr, ymax=solvable+solvableerr), colour="blue", width=.02) +
  geom_point(shape=21, size=1, colour="blue",  fill="white") +
  xlab ("fraction of pre-assigned elements") +
  ylab ("solvability") +
  geom_line(data=data.frame(x=c(-Inf, +Inf), y = c(0.5, .5)), aes(x=x, y=y), colour = "black", size=.5, linetype = "dashed") + 
  annotate("rect", xmin=psol1, xmax=psol2, ymin=-Inf, ymax=+Inf, fill='black', alpha=0.2) +
  geom_text(aes(x = psol1, y = 0, label = format(psol1, digits=2), vjust = "top", hjust ="right"), colour = "black") + 
  geom_text(aes(x = psol2, y = 1, label = format(psol2, digits=2), vjust = "bottom", hjust = "left"), colour = "black") + 
  theme_bw() 

show(figure_solvable)
ggsave("solvability.png", plot = figure_solvable, device = "png")
