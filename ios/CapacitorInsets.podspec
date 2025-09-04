Pod::Spec.new do |s|
  s.name         = 'CapacitorInsets'
  s.version      = '1.0.0'
  s.summary      = 'Expose safe area insets to Capacitor'
  s.license      = 'MIT'
  s.author       = 'chiwek'
  s.homepage     = 'https://github.com/chiwek/capacitor-insets'
  s.source       = { :git => 'https://github.com/chiwek/capacitor-insets.git', :tag => s.version.to_s }
  s.source_files = 'Plugin/**/*.{swift,h,m,c,mm}'
  s.dependency   'Capacitor'
  s.platform     = :ios, '13.0'
end
