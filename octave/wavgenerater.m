clear all;
bps = 8;
sps = 8000;
freq = 30;
[X,nsecs] = completer("2012_complete_sound.txt", bps);
%[X,nsecs] = completer("test.txt", bps);
nsamples = sps*(nsecs+1);

time = linspace(0,nsecs,nsamples);
wave = sin(2*pi*freq*time);
window=zeros(size(wave));
for i=1:nsecs
  k=sps*i;
  window(k+1:k+sps) = X(i)*ones(1,sps);
end

%size(wave)
%size(window)
wave = wave.*window;
wavwrite(wave',6*sps,bps,"out.wav");
