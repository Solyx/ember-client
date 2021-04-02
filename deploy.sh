unzip -o build/update.zip -d ../solyx.github.io/hafen/jars/
cd ../solyx.github.io/hafen/jars/
git add ./*
git commit -m "Update client files"
sh ./update-hashes.sh
git add ./*
git commit -m "Update client file listing"
git push
