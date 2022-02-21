analyze_data <- function  (filename) {
  # Reading the data
  rawdata = read.table(paste(filename, "rearrangement.txt", sep=""))
  # Compute basic statistics 
  stderr <- function(x) sd(x)/sqrt(length(x))
  m <- dim(rawdata)[1];
  n <- dim(rawdata)[2];
  even_ind <- seq(2, n, 2)
  dataframe <- data.frame(
    "size" = rawdata[,1],
    "ops" = apply(t(rawdata)[even_ind,], 2, mean)/rawdata[,1],
    "opserr" = apply(t(rawdata)[even_ind,], 2, stderr)/rawdata[,1]
  )
  
  
  # Power-law fit
  
  xx <- seq(min(rawdata[,1]), max(rawdata[,1]), by = .1)
  b0 <- log((1-dataframe$ops[m])/(1-dataframe$ops[m-1]))/log(dataframe$size[m]/dataframe$size[m-1])
  a0 <- (1-dataframe$ops[m])/(dataframe$size[m]^b0)
  datafit1 <- nls((1-ops) ~ a * size ^ b, data=dataframe, start = list(a = a0, b = b0))
  fitfun1 <- function(x) predict(datafit1, newdata=data.frame(size=x))
  pwrdata <- data.frame(
    "size" = xx,
    "pops"  = 1-fitfun1(xx)
  )
  
  
 
  
  
  # Graphical representation
  
  library(ggplot2)
  col = c("data" = "blue", "power-law fit" = "red")
  lab = c("data", "power-law fit")
  figure <- ggplot(data=dataframe, aes(x=size, y=ops)) + 
    geom_line(data = pwrdata, aes(x=size, y=pops, colour = "power-law fit")) +
    geom_errorbar(aes(ymin=ops-opserr, ymax=ops+opserr, colour="data"), width=1) +
    geom_point(shape=21, size=3, aes(colour="data"),  fill="white") +
    scale_colour_manual(name = "", breaks = lab, labels = lab, values = col ) + 
    guides(color = guide_legend(override.aes = list(shape = c(1, NA), 
                                                    linetype = c("blank", "solid")
    ) 
    )
    ) +
    ylab("operations / element") +
    xlab("number of elements") +
    ggtitle (filename) +
    theme_bw() + 
    theme(legend.justification = c(0, 1), 
          legend.position = c(.7, .3), 
          legend.box.margin=margin(c(5,5,5,5))) 
  
  show(figure)
  ggsave(paste(filename, ".png", sep=""), plot = figure, device = "png")
  
  cat("Power-law fit\n-------------\n")
  show(summary(datafit1))
}


analyze_data("PrefixSort")
analyze_data("BreakpointReversal")
