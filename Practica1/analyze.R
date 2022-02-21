# Reading the data
rawdata = read.table("stats.txt");
# Compute basic statistics 
dataframe <- data.frame(
  "digits" = rawdata[,1],
  "time" = apply(t(rawdata)[-1,], 2, median),
  "upp" = apply(t(rawdata)[-1,], 2, quantile, prob = .75), # the error bars span the 2nd and 3rd quartiles 
  "low" = apply(t(rawdata)[-1,], 2, quantile, prob = .25)
)


# Power-law fit

n <- dim(rawdata)[1];
xx <- seq(min(dataframe$digits), max(dataframe$digits), by = .01)
b0 <- log(dataframe$time[n]/dataframe$time[n-1])/log(dataframe$digits[n]/dataframe$digits[n-1])
a0 <- dataframe$time[n]/(dataframe$digits[n]^b0)
datafit1 <- nls(time ~ a * digits^b, data=dataframe, start = list(a = a0, b = b0))
fitfun1 <- function(x) predict(datafit1, newdata=data.frame(digits=x))
pwrdata <- data.frame(
  "digits" = xx,
  "ptime"  = fitfun1(xx)
)
  

# Exponential fit

b0 <- dataframe$time[n]/dataframe$time[n-1]
a0 <- dataframe$time[n]/(b0^dataframe$digits[n])
datafit2 <- nls(time ~ a * b^digits, data=dataframe, start = list(a = a0, b = b0))
fitfun2 <- function(x) predict(datafit2, newdata=data.frame(digits=x))
expdata <- data.frame(
  "digits" = xx,
  "ptime"  = fitfun2(xx)
)



# Graphical representation

library(ggplot2)
library(scales)
col = c("data" = "blue", "exponential fit" = "black", "power-law fit" = "red")
lab = c("data", "power-law fit", "exponential fit")
rx =  seq(min(dataframe$digits), max(dataframe$digits), by = 1)
figure <- ggplot(data=dataframe, aes(x=digits, y=time)) + 
  geom_line(data = pwrdata, aes(x=digits, y=ptime, colour = "power-law fit")) +
  geom_line(data = expdata, aes(x=digits, y=ptime, colour = "exponential fit")) +
  geom_errorbar(aes(ymin=low, ymax=upp, colour="data"), width=.2) +
  geom_point(shape=21, size=3, aes(colour="data"),  fill="white") +
  scale_colour_manual(name = "", breaks = lab, labels = lab, values = col ) + 
  guides(color = guide_legend(override.aes = list(shape = c(1, NA, NA), 
                                                  linetype = c("blank", "solid", "solid")
                                                  ) 
                              )
         ) +
  scale_x_continuous (name = "digits", breaks = rx) +
  scale_y_continuous(name = "time (s)",
                     trans = 'log10',
                     breaks = trans_breaks('log10', function(x) 10^x),
                     labels = trans_format('log10', math_format(10^.x)))+
  theme_bw() + 
  theme(legend.justification = c(0, 1), 
        legend.position = c(0, 1), 
        legend.box.margin=margin(c(5,5,5,5))) 

show(figure)
ggsave("factorization.png", plot = figure, device = "png")

cat("Power-law fit\n-------------\n")
show(summary(datafit1))

cat("Exponential fit\n---------------\n")
show(summary(datafit2))

