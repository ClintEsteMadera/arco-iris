#!/usr/bin/perl -w
#!/server/perl/bin/perl -Tw

use strict;
use Getopt::Std;
use IO::Handle;

sub usage() {
  print STDERR << "EOF";

This effector program turns the webserver on or off

 usage: $0 [option] -s on|off

  -h        : this (help) message
  -s on|off : on or off to start/stop webserver

EOF
  exit;
}

# get options
my %opts;
getopts('hs:', \%opts) or usage();
usage() if ($opts{h} || !defined($opts{s}));

my $switch = $opts{s};
usage() if ($switch ne "on" && $switch ne "off");

if ($switch eq "on") {
  print "\nStarting Apache...";
  print `/cygdrive/c/server/Apache2/bin/Apache.exe -w -n "Apache2" -k start`;
} else {
  print "\nStopping Apache...";
  print `/cygdrive/c/server/Apache2/bin/Apache.exe -w -n "Apache2" -k stop`;
}
print "\nDone.";
