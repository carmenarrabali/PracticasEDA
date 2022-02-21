# Reading the data
rawdata = read.table("Mediant-Approximation-stats.txt")
# Compute basic statistics 
dataframe <- data.frame(
  "precision" = 0.5*10^(-rawdata[,1]),
  "time" = apply(t(rawdata)[-1,], 2, median),
  "upp" = apply(t(rawdata)[-1,], 2, quantile, prob = .75), # the error bars span the 2nd and 3rd quartiles 
  "low" = apply(t(rawdata)[-1,], 2, quantile, prob = .25)
)


# Power-law fit

n <- dim(rawdata)[1];
xx <- 0.5*10^(-seq(min(rawdata[,1]), max(rawdata[,1]), by = .01))
b0 <- log(dataframe$time[n]/dataframe$time[n-1])/log(dataframe$precision[n]/dataframe$precision[n-1])
a0 <- dataframe$time[n]/(dataframe$precision[n]^b0)
datafit1 <- nls(time ~ a * precision^b, data=dataframe, start = list(a = a0, b = b0))
fitfun1 <- function(x) predict(datafit1, newdata=data.frame(precision=x))
pwrdata <- data.frame(
  "precision" = xx,
  "ptime"  = fitfun1(xx)
)


# Log fit

a0 <- dataframe$time[n]/log(dataframe$precision[n])
datafit2 <- nls(time ~ a * log(precision), data=dataframe, start = list(a = a0))
fitfun2 <- function(x) predict(datafit2, newdata=data.frame(precision=x))
logdata <- data.frame(
  "precision" = xx,
  "ptime"  = fitfun2(xx)
)



# Graphical representation

library(ggplot2)
library(scales)
col = c("data" = "blue", "logarithmic fit" = "black", "power-law fit" = "red")
lab = c("data", "power-law fit", "logarithmic fit")
figure <- ggplot(data=dataframe, aes(x=precision, y=time)) + 
  geom_line(data = pwrdata, aes(x=precision, y=ptime, colour = "power-law fit")) +
  geom_line(data = logdata, aes(x=precision, y=ptime, colour = "logarithmic fit")) +
  geom_errorbar(aes(ymin=low, ymax=upp, colour="data"), width=.2) +
  geom_point(shape=21, size=3, aes(colour="data"),  fill="white") +
  scale_colour_manual(name = "", breaks = lab, labels = lab, values = col ) + 
  guides(color = guide_legend(override.aes = list(shape = c(1, NA, NA), 
                                                  linetype = c("blank", "solid", "solid")
  ) 
  )
  ) +
  scale_y_continuous(name = "time (s)") +
  scale_x_continuous(name = "precision (\u03B5)",
                     trans = 'log10',
                     breaks = trans_breaks('log10', n = dim(rawdata)[1]+1, function(x) 10^x),
                     labels = trans_format('log10', math_format(10^.x)))+
  theme_bw() + 
  theme(legend.justification = c(0, 1), 
        legend.position = c(.7, 1), 
        legend.box.margin=margin(c(5,5,5,5))) 

show(figure)
ggsave("mediant-approximation.png", plot = figure, device = "png")

cat("Power-law fit\n-------------\n")
show(summary(datafit1))

cat("Logarithmic fit\n---------------\n")
show(summary(datafit2))

