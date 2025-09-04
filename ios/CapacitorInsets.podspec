Pod::Spec.new do |s|
  s.name         = 'CapacitorInsets'
  s.version      = '1.0.0'
  s.summary      = 'Expose safe area insets to Capacitor'
  s.license      = 'MIT'
  s.author       = 'You'
  s.homepage     = 'https://example.com'
  s.source       = { :git => 'https://example.com/repo.git', :tag => s.version.to_s }
  s.source_files = 'Plugin/**/*.{swift,h,m,c,mm}'
  s.dependency   'Capacitor'
  s.platform     = :ios, '13.0'
end
