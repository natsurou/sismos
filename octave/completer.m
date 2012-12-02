function [X,n] = completer(file,bps)
%% Reads and complete the secuense with 200 values
% file -- text where is the data.
% n -- number of point of interest
% X -- value between 1 and 7

y = load(file); % values with index
[m,k] = size(y);

for i=1:m
    lastsecond =(y(i,1)-mod(y(i,1),86400))/86400;
    X(lastsecond) = y(i,2);
    %X(y(i,1)) = y(i,2);
end
X = bps*X/60;
n = lastsecond;
end
