'use strict';

const gulp = require('gulp');
const sass = require('gulp-sass');
const uglifyes = require('uglify-es');
const composer = require('gulp-uglify/composer');
const minify = composer(uglifyes, console);

const scssSources = './src/sass/**/*.scss';
const htmlSources = './src/html/**/*.html';
const jsSources = './src/js/**/*.js';

gulp.task('sass', () => {
  return gulp.src(scssSources)
    .pipe(sass({outputStyle: 'compressed'}).on('error', sass.logError))
    .pipe(gulp.dest('./out/css'));
});

gulp.task('sass:watch', () => {
  return gulp.watch(scssSources, ['sass']);
});

gulp.task('html', () => {
  return gulp.src(htmlSources)
    .pipe(gulp.dest('./out'));
});

gulp.task('js', () => {
  return gulp.src(jsSources)
    .pipe(minify())
    .on('error', (err) => console.log('errrrrr', err.toString()))
    .pipe(gulp.dest('./out/js'));
});

gulp.task('watch', () => {
  gulp.watch(scssSources, ['sass']);
  gulp.watch(htmlSources, ['html']);
  gulp.watch(jsSources, ['js']);
});

gulp.task('build', ['sass', 'html', 'js']);
