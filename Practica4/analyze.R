# Reading the data
rawdata = read.table("folding.txt")
# Compute basic statistics 
dataframe <- data.frame(
  "size" = rawdata[,1],
  "time" = apply(t(rawdata)[-1,], 2, median),
  "upp" = apply(t(rawdata)[-1,], 2, quantile, prob = .75), # the error bars span the 2nd and 3rd quartiles 
  "low" = apply(t(rawdata)[-1,], 2, quantile, prob = .25)
)


# purely quadratic fit without intercept
xx <- seq(min(rawdata[,1]), max(rawdata[,1]), by = 1)
size2 <- dataframe$size^2
datafit1 <- lm(time ~ 0 + size2, data=dataframe)
fitfun1 <- function(x) predict(datafit1, newdata=data.frame(size2=x^2))
quadata <- data.frame(
  "size" = xx,
  "ptime"  = fitfun1(xx)
)


# purely cubic fit without intercept
size3 <- dataframe$size^3
datafit2 <- lm(time ~ 0 +  size3, data=dataframe)
fitfun2 <- function(x) predict(datafit2, newdata=data.frame(size3=x^3))
cubdata <- data.frame(
  "size" = xx,
  "ptime"  = fitfun2(xx)
)

# purely 4th-degree fit without intercept
size4 <- dataframe$size^4
datafit3 <- lm(time ~ 0 +  size4, data=dataframe)
fitfun3 <- function(x) predict(datafit3, newdata=data.frame(size4=x^4))
qquaddata <- data.frame(
  "size" = xx,
  "ptime"  = fitfun3(xx)
)

# Graphical representation

library(ggplot2)
col = c("data" = "blue", "cubic fit" = "black", "quadratic fit" = "red", "4th-degree fit" = "magenta")
lab = c("data", "quadratic fit", "cubic fit", "4th-degree fit")

figure <- ggplot(data=dataframe, aes(x=size, y=time)) + 
  geom_line(data = quadata, aes(x=size, y=ptime, colour = "quadratic fit")) +
  geom_line(data = cubdata, aes(x=size, y=ptime, colour = "cubic fit")) +
  geom_line(data = qquaddata, aes(x=size, y=ptime, colour = "4th-degree fit")) +
  geom_errorbar(aes(ymin=low, ymax=upp, colour="data"), width=.2) +
  geom_point(shape=21, size=3, aes(colour="data"),  fill="white") +
  scale_colour_manual(name = "", breaks = lab, labels = lab, values = col ) + 
  guides(color = guide_legend(override.aes = list(shape = c(1, NA, NA, NA), 
                                                  linetype = c("blank", "solid", "solid", "solid")
  ) 
  )
  ) +
  ylab("time (s)") +
  xlab("number of nucleotides") + 
  theme_bw() + 
  theme(legend.justification = c(0, 1), 
        legend.position = c(0, 1), 
        legend.box.margin=margin(c(5,5,5,5))) 

show(figure)
ggsave("folding.png", plot = figure, device = "png")

cat("Quadratic fit\n-------------\n")
show(summary(datafit1))

cat("Cubic fit\n------------------------\n")
show(summary(datafit2))

cat("4th-degree fit\n------------------------\n")
show(summary(datafit3))
